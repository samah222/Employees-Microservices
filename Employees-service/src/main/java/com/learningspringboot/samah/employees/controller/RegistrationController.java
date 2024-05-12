package com.learningspringboot.samah.employees.controller;

import com.learningspringboot.samah.employees.dto.MyUserDto;
import com.learningspringboot.samah.employees.dto.UserRegistrationDto;
import com.learningspringboot.samah.employees.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private MyUserService userService;

    @PostMapping("v1/auth/register")
    public MyUserDto createUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.createNewUserforAuthentication(userRegistrationDto);
        //return userRepository.save(user);
    }
}
