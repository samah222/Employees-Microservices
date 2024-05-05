package com.learningspringboot.samah.employees.mapping;

import com.learningspringboot.samah.employees.dto.UserDto;
import com.learningspringboot.samah.employees.dto.UserRegistrationDto;
import com.learningspringboot.samah.employees.model.User;

public class UserMapper {
public static User UserDtoToUser(UserDto dto){
    return User.builder()
            .id(dto.getUserId())
            .role(dto.getRole())
            .email(dto.getEmail())
            .employee(dto.getEmployee())
            .build();
}

public static UserDto UserToUserDto(User user){
    return UserDto.builder().
            userId(user.getId())
            .role(user.getRole())
            .employee(user.getEmployee())
            .email(user.getEmail())
            .build();
}

    public static User UserRegistrationDtoToUser(UserRegistrationDto registrationDto) {
        if (registrationDto == null) {
            throw new NullPointerException("The User Registration DTO should not be null");
        }
        return User.builder()
                .email(registrationDto.getEmail())
                .role(registrationDto.getRole())
                .password(registrationDto.getPassword())
                .build();
    }
}
