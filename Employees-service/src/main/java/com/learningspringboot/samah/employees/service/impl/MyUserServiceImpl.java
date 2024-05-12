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
import com.learningspringboot.samah.employees.repository.MyUserRepository;
import com.learningspringboot.samah.employees.repository.ResetVerificationTokenRepository;
import com.learningspringboot.samah.employees.repository.VerificationTokenRepository;
import com.learningspringboot.samah.employees.service.MyUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MyUserServiceImpl implements MyUserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MyUserRepository userRepository;
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
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<MyUser> getUserAllUsers() {
        return userRepository.findAll();
    }

    private boolean validateUserRegistrationDto(UserRegistrationDto userRegistrationDto) {
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
        if (Arrays.stream(UserRole.values()).noneMatch((t) -> t == userRegistrationDto.getRole()))
            throw new InvalidRoleException("Role is not valid");
        if (!EmailValidation.isValidEmail(userRegistrationDto.getEmail()))
            throw new InvalidDataException("Invalid email syntax");
        return true;
    }

    @Transactional
    @Override
    public MyUserDto addNewUser(UserRegistrationDto userRegistrationDto) {
        if (!validateUserRegistrationDto(userRegistrationDto))
            throw new InvalidDataException("User data not valid");
        MyUser myUser = UserMapper.UserRegistrationDtoToUser(userRegistrationDto);
        myUser.setRole(myUser.getRole() == null ? UserRole.EMPLOYEE : myUser.getRole());
        myUser.setPassword(userRegistrationDto.getPassword());
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUser.setTokens(1);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token);
        var savedVerificationToken = verificationTokenRepository.save(verificationToken);
        myUser.setVerificationToken(savedVerificationToken);
        MyUser savedMyUser = userRepository.save(myUser);
        sendEmailForAddingNewUser(token, savedMyUser);
        return UserMapper.UserToUserDto(savedMyUser);
    }

    private void sendEmailForAddingNewUser(String token, MyUser savedMyUser) {
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/verifyRegistration?userid=" + savedMyUser.getId().toString() + "&token=" + token;
        producer.sendJsonMessage(new Mail(savedMyUser.getEmail(), "Registration token for ABC Company",
                "Welcome to our company !! please click in the following token to complete the registration: " + url));
    }

    @Transactional
    @Override
    public CustomResponse validateVerificationToken(String token, int userId) {
        if (token == null
                || token.isBlank())
            throw new InvalidDataException("Token can not be empty");
        if (userId < 0)
            throw new InvalidDataException("UserId is not valid");
        MyUser user = getUser(userId);
        VerificationToken verificationToken = verificationTokenRepository.findById(user.getVerificationToken().getId())
                .orElseThrow(() -> new InvalidDataException("User token not found"));
        if (!verificationToken.getToken().equals(token))
            return new CustomResponse(UserUtils.REGISTRATION_TOKEN_NOT_VALID, "REGISTRATION_TOKEN_NOT_VALID");
        if (TokenExpirationTime.calculateDifferenceTime(verificationToken.getExpirationTime(), LocalDateTime.now()) == 1) {
            user.setEnabled(true);
            userRepository.save(user);
            sendEmailAfterValidateVerificationToken(user);
            return new CustomResponse(UserUtils.SUCCESSFUL, "SUCCESSFUL");
        } else {
            verificationTokenRepository.deleteById(verificationToken.getId());
            return new CustomResponse(UserUtils.REGISTRATION_TOKEN_EXPIRED, "REGISTRATION_TOKEN_EXPIRED");
        }

    }

    private void sendEmailAfterValidateVerificationToken(MyUser user) {
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/employee/addEmployeeForm?userid=" + user.getId().toString();
        producer.sendJsonMessage(new Mail(user.getEmail(), "Registration Successful for ABC Company",
                "Your verification token is validated !! please click in the following link to complete the registration: " + url));
    }

    @Transactional
    @Override
    public MyUserDto updateUser(Integer id, MyUserDto dto) {
        MyUser user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if (!user.isEnabled())
            throw new UserNotActivatedException("User not activated");
        if (dto.getEmail() != null) {
            if (EmailValidation.isValidEmail(dto.getEmail()))
                user.setEmail(dto.getEmail());
            else throw new InvalidDataException("Email syntax is invalid");
        }
        if (dto.getRole() != null) {
            if (Arrays.stream(UserRole.values()).anyMatch((t) -> t == dto.getRole()))
                user.setRole(dto.getRole());
            else throw new InvalidDataException("Role not invalid");
        }

        userRepository.save(user);
        return UserMapper.UserToUserDto(user);
    }

    @Transactional
    @Override
    public CustomResponse resendVerificationToken(String email) {
        MyUser myUser = userRepository.findByEmail(email);
        if (myUser == null)
            throw new UserNotFoundException("User not found");
        if (myUser.isEnabled())
            throw new InvalidDataException("Your account is already active");
        if (myUser.getTokens() == UserUtils.MAX_NO_OF_TOKENS)
            return new CustomResponse(UserUtils.Max_NUMBER_OF_TOKENS, "Max_NUMBER_OF_TOKENS");
        myUser.setTokens(myUser.getTokens() + 1);
        VerificationToken verificationToken = verificationTokenRepository.findById(myUser.getVerificationToken().getId())
                .orElseThrow(() -> new InvalidDataException("Token not found"));
        if (verificationToken != null)
            verificationTokenRepository.deleteById(verificationToken.getId());
        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);
        VerificationToken savedVerificationToken = verificationTokenRepository.save(verificationToken);
        myUser.setVerificationToken(savedVerificationToken);
        userRepository.save(myUser);
        sendEmailForResendVerificationToken(email, token);
        return new CustomResponse(UserUtils.SUCCESSFUL, "Successful");
    }

    private void sendEmailForResendVerificationToken(String email, String token) {
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/newToken?token=" + token;
        producer.sendJsonMessage(new Mail(email, "BookStore Registration Resend Token",
                " Please click on this url to confirm your email : " + url));
    }

    @Transactional
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

    @Transactional
    @Override
    public CustomResponse requestResetPassword(RequestResetPasswordDto requestResetPasswordDto) {
        String token = null;
        if (requestResetPasswordDto == null
                || requestResetPasswordDto.getEmail() == null)
            throw new NullPointerException("Reset password data can not be null");
        MyUser user = userRepository.findByEmail(requestResetPasswordDto.getEmail());
        if (user == null)
            throw new UserNotFoundException("User not found");
        ResetVerificationToken resetToken = resetVerificationTokenRepository.findByMyUserId(user.getId());
        if (resetToken != null)
            token = resetToken.getToken();
        else {
            token = UUID.randomUUID().toString();
        }
        saveResetVerificationToken(user, token);
        sendEmailForRequestResetPassword(token, user);
        return new CustomResponse(UserUtils.SUCCESSFUL, "Email Sent");
    }

    private void sendEmailForRequestResetPassword(String token, MyUser user) {
        String url = "http://" + infoService.getApplicationServer() + ":" + infoService.getServerPort()
                + "/v1/users/resetPassword?token=" + token;
        producer.sendJsonMessage(new Mail(user.getEmail(), "BookStore- Token for Request Reset Password"
                , " Please click on this url to reset your password : " + url));
    }

    @Transactional
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
        MyUser myUser = getUser(id);
//        myUser.setEnabled(false);
//        userRepository.save(myUser);
        userRepository.deleteById(id);
    }

    @Override
    public List<MyUserDto> getAllUsers() {
        List<MyUser> myUsers = userRepository.findAll();
        return myUsers.stream().map(u -> UserMapper.UserToUserDto(u)).toList();
    }

    @Transactional
    @Override
    public MyUserDto createNewUserforAuthentication(UserRegistrationDto userRegistrationDto) {
        if (!validateUserRegistrationDto(userRegistrationDto))
            throw new InvalidDataException("User data not valid");
        MyUser myUser = UserMapper.UserRegistrationDtoToUser(userRegistrationDto);
        myUser.setRole(myUser.getRole() == null ? UserRole.EMPLOYEE : myUser.getRole());
        myUser.setPassword(userRegistrationDto.getPassword());
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        MyUser savedMyUser = userRepository.save(myUser);
        return UserMapper.UserToUserDto(savedMyUser);

    }

    @Override
    @Cacheable(value = "userById", key = "#id")
    public MyUserDto getUserById(Integer id) {
        MyUser myUser = getUser(id);
        return UserMapper.UserToUserDto(myUser);
    }

    public void saveVerificationTokenForUSer(MyUserDto dto, String token) {
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
