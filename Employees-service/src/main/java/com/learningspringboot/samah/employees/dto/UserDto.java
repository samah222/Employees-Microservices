package com.learningspringboot.samah.employees.dto;

import com.learningspringboot.samah.employees.Util.UserRole;
import com.learningspringboot.samah.employees.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private Integer userId;
    private String email;
    private UserRole role;
    private Employee employee;
}
