package com.learningspringboot.samah.employees.mapping;

import com.learningspringboot.samah.employees.dto.UserDto;
import com.learningspringboot.samah.employees.dto.UserRegistrationDto;
import com.learningspringboot.samah.employees.model.MyUser;

public class UserMapper {
public static MyUser UserDtoToUser(UserDto dto){
    return MyUser.builder()
            .id(dto.getUserId())
            .role(dto.getRole())
            .email(dto.getEmail())
            .employee(dto.getEmployee())
            .build();
}

public static UserDto UserToUserDto(MyUser myUser){
    return UserDto.builder().
            userId(myUser.getId())
            .role(myUser.getRole())
            .employee(myUser.getEmployee())
            .email(myUser.getEmail())
            .build();
}

    public static MyUser UserRegistrationDtoToUser(UserRegistrationDto registrationDto) {
        if (registrationDto == null) {
            throw new NullPointerException("The MyUser Registration DTO should not be null");
        }
        return MyUser.builder()
                .email(registrationDto.getEmail())
                .role(registrationDto.getRole())
                .password(registrationDto.getPassword())
                .build();
    }
}
