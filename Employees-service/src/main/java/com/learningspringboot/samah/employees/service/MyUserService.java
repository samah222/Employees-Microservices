package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.dto.*;
import com.learningspringboot.samah.employees.model.MyUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MyUserService {
    MyUser getUser(int id);

    List<MyUser> getUserAllUsers();

    MyUserDto addNewUser(UserRegistrationDto userRegistrationDto);

    public CustomResponse validateVerificationToken(String token, int userId);

    public MyUserDto updateUser(Integer id, MyUserDto dto);

    CustomResponse resendVerificationToken(String email);

    public CustomResponse changePassword(ChangePasswordDto changePasswordDto);

    public CustomResponse requestResetPassword(RequestResetPasswordDto requestResetPasswordDto);

    public CustomResponse resetPassword(ResetPasswordDto resetPasswordDto);

    public MyUserDto getUserById(Integer id);

    public void deleteUserById(Integer id);

    public List<MyUserDto> getAllUsers();

    MyUserDto createNewUserforAuthentication(UserRegistrationDto userRegistrationDto);
}
