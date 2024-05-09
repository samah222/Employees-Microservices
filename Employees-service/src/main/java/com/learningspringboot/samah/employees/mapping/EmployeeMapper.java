package com.learningspringboot.samah.employees.mapping;

import com.learningspringboot.samah.employees.dto.EmployeeDto;
import com.learningspringboot.samah.employees.model.Employee;
import com.learningspringboot.samah.employees.model.MyUser;

public class EmployeeMapper {
    public static EmployeeDto employeeToEmployeeDto(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setEmployeeType(employee.getEmployeeType());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setPhone(employee.getPhone());

        MyUser myUser = employee.getMyUser();
        if (myUser != null) {
            dto.setEmail(myUser.getEmail());
            dto.setRole(myUser.getRole());
            dto.setUserId(myUser.getId());
        }

        dto.setManager(employee.getManager());
        dto.setAddress(employee.getAddress());
        dto.setDepartment(employee.getDepartment());
        dto.setJobTitle(employee.getJobTitle());
        dto.setProjects(employee.getProjects());

        return dto;
    }

    public static Employee employeeDtoToEmployee(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setEmployeeName(dto.getEmployeeName());
        employee.setAddress(dto.getAddress());
        employee.setEmployeeType(dto.getEmployeeType());
        employee.setProjects(dto.getProjects());
        employee.setPhone(dto.getPhone());
        employee.setManager(dto.getManager());
        employee.setJobTitle(dto.getJobTitle());
        employee.setDepartment(dto.getDepartment());

        if (dto.getUserId() != null) {
            employee.setMyUser(MyUser.builder().id(dto.getUserId()).build());
        }

        return employee;
    }
}
