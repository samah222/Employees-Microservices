package com.learningspringboot.samah.employees.mapping;

import com.learningspringboot.samah.employees.dto.UserDto;
import com.learningspringboot.samah.employees.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
public static User toUser(UserDto dto){
    return User.builder()
            .id(dto.getUserId())
            .username(dto.getUsername())
            .role(dto.getRole())
            .employee(dto.getEmployee())
            .build();
}

public static UserDto toDto(User user){
    return UserDto.builder().
            userId(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .employee(user.getEmployee())
            .build();
}
}
