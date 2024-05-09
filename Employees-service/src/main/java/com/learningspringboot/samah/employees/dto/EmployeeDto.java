package com.learningspringboot.samah.employees.dto;

import com.learningspringboot.samah.employees.Util.EmployeeType;
import com.learningspringboot.samah.employees.Util.UserRole;
import com.learningspringboot.samah.employees.model.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EmployeeDto {
    private Integer Id;
    private String employeeName;
    private String phone;
    private String jobTitle;
    private Department department;
    private Address address;
    private Employee manager;
    private List<Project> projects;
    private Integer userId;
    private EmployeeType employeeType ;
    private UserRole role;
    private String email;
}
