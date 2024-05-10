package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.Util.*;
import com.learningspringboot.samah.employees.dto.*;
import com.learningspringboot.samah.employees.exception.*;
import com.learningspringboot.samah.employees.mapping.UserMapper;
import com.learningspringboot.samah.employees.model.ChangePasswordVerificationToken;
import com.learningspringboot.samah.employees.model.MyUser;
import com.learningspringboot.samah.employees.model.ResetVerificationToken;
import com.learningspringboot.samah.employees.model.VerificationToken;
import com.learningspringboot.samah.employees.publisher.RabbitmqMailProducer;
import com.learningspringboot.samah.employees.repository.ChangePasswordVerificationTokenRepository;
import com.learningspringboot.samah.employees.repository.ResetVerificationTokenRepository;
import com.learningspringboot.samah.employees.repository.UserRepository;
import com.learningspringboot.samah.employees.repository.VerificationTokenRepository;
import com.learningspringboot.samah.employees.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserImpl implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
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

    public static boolean findUserRole(String userRole) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(userRole)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MyUser getUser(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("myUser not found"));
    }

    @Override
    public List<MyUser> getUserAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto addNewUser(UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto == null
                || userRegistrationDto.getEmail() == null
                || userRegistrationDto.getPassword() == null
                || userRegistrationDto.getMatchingPassword() == null)
            throw new NullPointerException("Missing data");
        if (userRegistrationDto.getEmail().isBlank()
                || userRegistrationDto.getPassword().isBlank()
                || userRegistrationDto.getMatchingPassword().isBlank())
            throw new NullPointerException("User data can not be empty");
        if (userRepository.findByEmail(userRegistrationDto.getEmail()) != null)
            throw new UsernameAlreadyExistsException("Email already taken");
        if (!PasswordValidator.isPasswordStrong(userRegistrationDto.getPassword()))
            throw new WeakPasswordException("Password should be at least 8 characters, one Capital, number and" +
                    "a special character");
        System.out.println("before get role ");
        if (Arrays.stream(UserRole.values()).noneMatch((t) -> t == userRegistrationDto.getRole()))
            throw new InvalidRoleException("Role is not valid");
        if (!EmailValidation.isValidEmail(userRegistrationDto.getEmail()))
            throw new InvalidDataException("Invalid email syntax");
        MyUser myUser = UserMapper.UserRegistrationDtoToUser(userRegistrationDto);
        myUser.setRole(myUser.getRole() == null ? UserRole.EMPLOYEE : myUser.getRole());
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUser.setTokens(1);
        MyUser savedMyUser = userRepository.save(myUser);
        String token = UUID.randomUUID().toString();
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/verifyRegistration?token=" + token;
        saveVerificationToken(savedMyUser, token);
        producer.sendJsonMessage(new Mail(myUser.getEmail(), "Registration token for ABC Company",
                "Welcome to our company !! please click in the following token to complete the registration: " + url));
        return UserMapper.UserToUserDto(savedMyUser);
    }

    @Override
    public CustomResponse validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null
                || verificationToken.getToken().isBlank())
            return new CustomResponse(UserUtils.REGISTRATION_TOKEN_NOT_FOUND, "REGISTRATION_TOKEN_NOT_FOUND");
        else {
            MyUser myUser = verificationToken.getMyUser();
            if (TokenExpirationTime.calculateDifferenceTime(verificationToken.getExpirationTime(), LocalDateTime.now()) == 1) {
                myUser.setEnabled(true);
                userRepository.save(myUser);
                String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                        + "/v1/employee/addEmployeeForm?userid=" + myUser.getId().toString();

                producer.sendJsonMessage(new Mail(myUser.getEmail(), "Registration Successful for ABC Company",
                        "Your verification token is validated !! please click in the following link to complete the registration: " + url));

                return new CustomResponse(UserUtils.SUCCESSFUL, "SUCCESSFUL");
            } else {
                verificationTokenRepository.deleteById(verificationToken.getId());
                return new CustomResponse(UserUtils.REGISTRATION_TOKEN_EXPIRED, "REGISTRATION_TOKEN_EXPIRED");
            }
        }
    }

    @Override
    public UserDto updateUser(Integer id, UserDto dto) {
        MyUser dbMyUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if (!dbMyUser.isEnabled())
            throw new UserNotActivatedException("User not activated");
        Optional.ofNullable(dto.getEmail()).ifPresent(data -> {
            if (EmailValidation.isValidEmail(data))
                dbMyUser.setEmail(data);
            else throw new InvalidDataException("Email syntax is invalid");
        });
        Optional.ofNullable(dto.getRole()).ifPresent(data -> {
            if (Arrays.stream(UserRole.values()).anyMatch((t) -> t == dto.getRole()))
                dbMyUser.setRole(data);
            else throw new InvalidDataException("Role not invalid");
        });
        userRepository.save(dbMyUser);
        return UserMapper.UserToUserDto(dbMyUser);
    }

    @Override
    public CustomResponse resendVerificationToken(String email) {
        MyUser myUser = userRepository.findByEmail(email);
        if (myUser == null)
            throw new UserNotFoundException("User not found");
        if (myUser.getTokens() == UserUtils.MAX_NO_OF_TOKENS)
            return new CustomResponse(UserUtils.Max_NUMBER_OF_TOKENS, "Max_NUMBER_OF_TOKENS");
        VerificationToken verificationToken = verificationTokenRepository.findByMyUserId(myUser.getId());
        if (verificationToken != null)
            verificationTokenRepository.deleteById(verificationToken.getId());
        String token = UUID.randomUUID().toString();
        saveVerificationToken(myUser, token);
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/newToken?token=" + token;
        producer.sendJsonMessage(new Mail(email, "BookStore Registration Resend Token",
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
        if (!PasswordValidator.isPasswordStrong(changePasswordDto.getNewPassword()))
            throw new WeakPasswordException("Password should be at least 8 characters, one Capital, number and" +
                    " a special character");
        MyUser myUser = userRepository.findByEmail(changePasswordDto.getEmail());
        if (myUser == null)
            throw new UserNotFoundException("MyUser not exist");
        if (!myUser.getPassword().equals(changePasswordDto.getOldPassword())) {
            return new CustomResponse(UserUtils.OLD_PASSWORD_NOT_MATCHING_WITH_DB, "OLD_PASSWORD_NOT_MATCHING");
        } else {
            myUser.setPassword(changePasswordDto.getNewPassword());
            userRepository.save(myUser);
        }
        return new CustomResponse(0, "SUCCESSFUL");
    }

    @Override
    public CustomResponse requestResetPassword(RequestResetPasswordDto requestResetPasswordDto) {
        String token = null;
        if (requestResetPasswordDto == null
                || requestResetPasswordDto.getEmail() == null)
            throw new NullPointerException("Reset password data can not be null");
        MyUser myUser = userRepository.findByEmail(requestResetPasswordDto.getEmail());
        if (myUser == null)
            throw new UserNotFoundException("User not found");
        ResetVerificationToken resetToken = resetVerificationTokenRepository.findByMyUserId(myUser.getId());
        if (resetToken != null)
            token = resetToken.getToken();
        else {
            token = UUID.randomUUID().toString();
        }
        saveResetVerificationToken(myUser, token);
        // send email
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/resetPassword?token=" + token;
        producer.sendJsonMessage(new Mail(myUser.getEmail(), "BookStore- Token for Request Reset Password"
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
            throw new InvalidDataException("Passwords not matched");
        MyUser myUser = userRepository.findByEmail(resetPasswordDto.getEmail());
        if (myUser == null)
            throw new UserNotFoundException("User email not found");
        ResetVerificationToken resetVerificationToken = resetVerificationTokenRepository.findByMyUserId(myUser.getId());
        if (resetVerificationToken == null)
            return new CustomResponse(UserUtils.RESET_TOKEN_NOT_FOUND, "RESET_TOKEN_NOT_FOUND");
        if (!resetVerificationToken.getToken().equals(resetPasswordDto.getToken()))
            return new CustomResponse(UserUtils.RESET_TOKEN_NOT_MATCHED, "RESET_TOKEN_NOT_MATCHED");
        if (TokenExpirationTime.calculateDifferenceTime(resetVerificationToken.getExpirationTime(), LocalDateTime.now()) != 1)
            return new CustomResponse(UserUtils.RESET_TOKEN_EXPIRED, "RESET_TOKEN_EXPIRED");
        myUser.setPassword(resetPasswordDto.getPassword());
        userRepository.save(myUser);
        return new CustomResponse(0, "SUCCESSFUL");
    }

    @Override
    public void deleteUserById(Integer id) {
        MyUser myUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
//        myUser.setEnabled(false);
//        userRepository.save(myUser);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<MyUser> myUsers = userRepository.findAll();
        return myUsers.stream().map(u -> UserMapper.UserToUserDto(u)).toList();
    }

    @Override
    @Cacheable(value = "userById", key = "#id")
    public UserDto getUserById(Integer id) {
        MyUser myUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserMapper.UserToUserDto(myUser);
    }

    public void saveVerificationTokenForUSer(UserDto dto, String token) {
        VerificationToken verificationToken = new VerificationToken(UserMapper.UserDtoToUser(dto), token);
        verificationTokenRepository.save(verificationToken);
    }

    public void saveVerificationToken(MyUser myUser, String token) {
        VerificationToken verificationToken = new VerificationToken(myUser, token);
        verificationTokenRepository.save(verificationToken);
    }

    public void saveChangePasswordVerificationToken(MyUser myUser, String token) {
        ChangePasswordVerificationToken changeToken = new ChangePasswordVerificationToken(myUser, token);
        changePasswordVerificationTokenRepository.save(changeToken);
    }

    public void saveResetVerificationToken(MyUser myUser, String token) {
        ResetVerificationToken resetToken = new ResetVerificationToken(myUser, token);
        resetVerificationTokenRepository.save(resetToken);
    }
}
