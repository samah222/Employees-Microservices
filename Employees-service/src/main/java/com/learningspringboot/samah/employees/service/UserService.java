package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.dto.*;
import com.learningspringboot.samah.employees.model.MyUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
     MyUser getUser(int id);

    List<MyUser> getUserAllUsers();

    UserDto addNewUser(UserRegistrationDto userRegistrationDto);
    public CustomResponse validateVerificationToken(String token);
    public UserDto updateUser(Integer id, UserDto dto);
    CustomResponse resendVerificationToken(String email);
    public CustomResponse changePassword(ChangePasswordDto changePasswordDto) ;
    public CustomResponse requestResetPassword(RequestResetPasswordDto requestResetPasswordDto);
    public CustomResponse resetPassword(ResetPasswordDto resetPasswordDto);
    public UserDto getUserById(Integer id) ;
    public void deleteUserById(Integer id) ;

    public List<UserDto> getAllUsers() ;
    }
