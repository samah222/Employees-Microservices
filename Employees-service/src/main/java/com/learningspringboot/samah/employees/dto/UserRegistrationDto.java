package com.learningspringboot.samah.employees.dto;

import com.learningspringboot.samah.employees.Util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String password;
    private String matchingPassword;
    private UserRole role;
}
