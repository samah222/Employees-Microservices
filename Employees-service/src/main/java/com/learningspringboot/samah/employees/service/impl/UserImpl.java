package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.Util.*;
import com.learningspringboot.samah.employees.dto.*;
import com.learningspringboot.samah.employees.exception.*;
import com.learningspringboot.samah.employees.mapping.UserMapper;
import com.learningspringboot.samah.employees.model.User;
import com.learningspringboot.samah.employees.model.VerificationToken;
import com.learningspringboot.samah.employees.model.ChangePasswordVerificationToken;
import com.learningspringboot.samah.employees.model.ResetVerificationToken;
import com.learningspringboot.samah.employees.publisher.RabbitmqMailProducer;
import com.learningspringboot.samah.employees.repository.ChangePasswordVerificationTokenRepository;
import com.learningspringboot.samah.employees.repository.ResetVerificationTokenRepository;
import com.learningspringboot.samah.employees.repository.UserRepository;
import com.learningspringboot.samah.employees.repository.VerificationTokenRepository;
import com.learningspringboot.samah.employees.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private ResetVerificationTokenRepository resetVerificationTokenRepository;
    @Autowired
    private ChangePasswordVerificationTokenRepository changePasswordVerificationTokenRepository;
    @Autowired
    private RabbitmqMailProducer producer;
    @Autowired
    private InfoServiceImpl infoService;
    @Override
    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    @Override
    public List<User> getUserAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto addNewUser(UserRegistrationDto userRegistrationDto) {
        System.out.println("start addNewUser ");
        if(userRegistrationDto==null
        || userRegistrationDto.getEmail()==null
        || userRegistrationDto.getPassword()==null
        || userRegistrationDto.getMatchingPassword() == null)
            throw new NullPointerException("Missing data");
        if(userRepository.findByEmail(userRegistrationDto.getEmail())!= null)
            throw new UsernameAlreadyExistsException("Email already taken");
        if(!PasswordValidator.isPasswordStrong(userRegistrationDto.getPassword()))
            throw new WeakPasswordException("Password should be at least 8 characters, one Capital, number and" +
                    "a special character");
        System.out.println("before get role ");
        if(Arrays.stream(UserRole.values()).noneMatch((t) -> t == userRegistrationDto.getRole() ))
              throw new InvalidRoleException("Role is not valid");
        System.out.println("after get role ");
        if(!EmailValidation.isValidEmail(userRegistrationDto.getEmail()))
            throw new InvalidUserDataException("Invalid email syntax");
        User user = UserMapper.UserRegistrationDtoToUser(userRegistrationDto);
        user.setRole(user.getRole()==null? UserRole.EMPLOYEE:user.getRole());
        user.setTokens(user.getTokens()+1);
        User savedUser = userRepository.save(user);
        String token = UUID.randomUUID().toString();
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/verifyRegistration?token=" + token;
        saveVerificationToken(savedUser, token);
        producer.sendJsonMessage(new Mail(user.getEmail(),"Registration token for ABC Company",
                "Welcome to our company !! please click in the following token to complete the registration: "+url));

        return UserMapper.UserToUserDto(savedUser);
    }

    @Override
    public CustomResponse validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null)
            return new CustomResponse(UserUtils.REGISTRATION_TOKEN_NOT_FOUND, "REGISTRATION_TOKEN_NOT_FOUND");
        else {
            User user = verificationToken.getUser();
            if (TokenExpirationTime.calculateDifferenceTime(verificationToken.getExpirationTime(), LocalDateTime.now()) == 1) {
                user.setEnabled(true);
                userRepository.save(user);
                return new CustomResponse(UserUtils.SUCCESSFUL, "SUCCESSFUL");
            } else {
                verificationTokenRepository.deleteById(verificationToken.getId());
                return new CustomResponse(UserUtils.REGISTRATION_TOKEN_EXPIRED, "REGISTRATION_TOKEN_EXPIRED");
            }
        }
    }

    @Override
    public UserDto updateUser(Integer id, UserDto dto) {
        User dbUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if(!dbUser.isEnabled())
            throw new UserNotActivatedException("User not activated");
        Optional.ofNullable(dto.getEmail()).ifPresent(data -> {
            if(EmailValidation.isValidEmail(data))
                dbUser.setEmail(dto.getEmail());
            else throw new InvalidUserDataException("Email syntax is invalid");
        });
        Optional.ofNullable(dto.getRole()).ifPresent(data -> {
            if(Arrays.stream(UserRole.values()).anyMatch((t) -> t == dto.getRole()))
                dbUser.setRole(dto.getRole());
            else throw new InvalidUserDataException("Role not invalid");
        });
        userRepository.save(dbUser);
        return UserMapper.UserToUserDto(dbUser);
    }

    @Override
    public CustomResponse resendVerificationToken(String email) {
        User user = userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotFoundException("User not found");
        if (user.getTokens() == UserUtils.MAX_NO_OF_TOKENS)
            return new CustomResponse(UserUtils.Max_NUMBER_OF_TOKENS, "Max_NUMBER_OF_TOKENS");
        VerificationToken verificationToken = verificationTokenRepository.findByUserId(user.getId());
        if(verificationToken != null)
            verificationTokenRepository.deleteById(verificationToken.getId());
        String token = UUID.randomUUID().toString();
        saveVerificationToken(user, token);
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/newToken?token=" + token;
        producer.sendJsonMessage(new Mail(email,"BookStore Registration Resend Token",
                " Please click on this url to confirm your email : " + url));
        return new CustomResponse(UserUtils.SUCCESSFUL, "Successful");
    }

    @Override
    public CustomResponse changePassword(ChangePasswordDto changePasswordDto) {
        if (changePasswordDto.getOldPassword() == null
                || changePasswordDto.getNewPassword() == null
                || changePasswordDto.getMatchingPassword() == null
                || changePasswordDto.getEmail() == null)
            throw new NullPointerException("Change password info can not be null");
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getMatchingPassword())) {
            return new CustomResponse(UserUtils.NEW_PASSWORDS_NOT_MATCHING, "PASSWORDS_NOT_MATCHING");
        }
        if(!PasswordValidator.isPasswordStrong(changePasswordDto.getNewPassword()))
            throw new WeakPasswordException("Password should be at least 8 characters, one Capital, number and" +
                    " a special character");
        User user = userRepository.findByEmail(changePasswordDto.getEmail());
        if( user == null)
                throw new UserNotFoundException("User not exist");
        if (!user.getPassword().equals(changePasswordDto.getOldPassword())) {
            return new CustomResponse(UserUtils.OLD_PASSWORD_NOT_MATCHING_WITH_DB, "OLD_PASSWORD_NOT_MATCHING");
        } else {
            user.setPassword(changePasswordDto.getNewPassword());
            userRepository.save(user);
        }
        return new CustomResponse(0, "SUCCESSFUL");
    }

    @Override
    public CustomResponse requestResetPassword(RequestResetPasswordDto requestResetPasswordDto) {
        String token = null;
        if (requestResetPasswordDto == null
                || requestResetPasswordDto.getEmail() == null)
            throw new NullPointerException("Reset password data can not be null");
        User user = userRepository.findByEmail(requestResetPasswordDto.getEmail());
        if(user ==null)
               throw new UserNotFoundException("User not found");
        ResetVerificationToken resetToken = resetVerificationTokenRepository.findByUserId(user.getId());
        if (resetToken != null)
            token = resetToken.getToken();
        else {
            token = UUID.randomUUID().toString();
        }
        saveResetVerificationToken(user, token);
        // send email
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/resetPassword?token=" + token;
        producer.sendJsonMessage(new Mail(user.getEmail(), "BookStore- Token for Request Reset Password"
                , " Please click on this url to reset your password : " + url));
        return new CustomResponse(UserUtils.SUCCESSFUL, "Email Sent");
    }

    @Override
    public CustomResponse resetPassword(ResetPasswordDto resetPasswordDto) {
        if (resetPasswordDto == null
                || resetPasswordDto.getEmail() == null
                || resetPasswordDto.getPassword() == null
                || resetPasswordDto.getMatchingPassword() == null
                || resetPasswordDto.getToken() == null)
            throw new NullPointerException("Reset password data can not be null");
        if (!resetPasswordDto.getPassword().equals(resetPasswordDto.getMatchingPassword()))
            throw new InvalidUserDataException("Passwords not matched");
        User user = userRepository.findByEmail(resetPasswordDto.getEmail());
        if(user==null)
                throw new UserNotFoundException("User email not found");
        ResetVerificationToken resetVerificationToken = resetVerificationTokenRepository.findByUserId(user.getId());
        if (resetVerificationToken == null)
            return new CustomResponse(UserUtils.RESET_TOKEN_NOT_FOUND, "RESET_TOKEN_NOT_FOUND");
        if (!resetVerificationToken.getToken().equals(resetPasswordDto.getToken()))
            return new CustomResponse(UserUtils.RESET_TOKEN_NOT_MATCHED, "RESET_TOKEN_NOT_MATCHED");
        if (TokenExpirationTime.calculateDifferenceTime(resetVerificationToken.getExpirationTime(), LocalDateTime.now()) != 1)
            return new CustomResponse(UserUtils.RESET_TOKEN_EXPIRED, "RESET_TOKEN_EXPIRED");
        user.setPassword(resetPasswordDto.getPassword());
        userRepository.save(user);
        return new CustomResponse(0, "SUCCESSFUL");
    }

    @Override
    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> UserMapper.UserToUserDto(u)).toList();
    }

    @Override
    @Cacheable(value = "userById", key = "#id")
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserMapper.UserToUserDto(user);
    }

    public void saveVerificationTokenForUSer(UserDto dto, String token) {
        VerificationToken verificationToken = new VerificationToken(UserMapper.UserDtoToUser(dto), token);
        verificationTokenRepository.save(verificationToken);
    }

    public void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    public void saveChangePasswordVerificationToken(User user, String token) {
        ChangePasswordVerificationToken changeToken = new ChangePasswordVerificationToken(user, token);
        changePasswordVerificationTokenRepository.save(changeToken);
    }

    public void saveResetVerificationToken(User user, String token) {
        ResetVerificationToken resetToken = new ResetVerificationToken(user, token);
        resetVerificationTokenRepository.save(resetToken);
    }
    public static boolean findUserRole(String userRole) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(userRole)) {
                return true;
            }
        }
        return false;
    }
}
