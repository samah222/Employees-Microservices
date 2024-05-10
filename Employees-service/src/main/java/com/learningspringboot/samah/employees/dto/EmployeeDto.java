package com.learningspringboot.samah.employees.dto;

import com.learningspringboot.samah.employees.Util.EmployeeType;
import com.learningspringboot.samah.employees.model.Address;
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
    private Integer departmentId;
    private Address address;
    private Integer managerId;
    private List<Integer> projectsIds;
    private Integer userId;
    private EmployeeType employeeType;
    private Double salary;
    private Double bonus;
    private Double hourlyRate;
    private Integer hoursWorkedPerWeek;
}
