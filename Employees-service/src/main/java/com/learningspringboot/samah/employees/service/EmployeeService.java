package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.dto.EmployeeDto;
import com.learningspringboot.samah.employees.model.Employee;

import java.util.List;

public interface EmployeeService {
    public List<EmployeeDto> getAllEmployees(int pageNumber, int pageSize);

    public List<Employee> getAllEmployees();

    public EmployeeDto addEmployee(EmployeeDto employeeDto);

    public EmployeeDto editEmployee(EmployeeDto employeeDto);

    public EmployeeDto getEmployee(int id);

    public void deleteEmployee(int Id);

    List<Employee> getEmployeesByDepartment(String department);

    //List<Employee> getEmployeesBySalaryBetween(double min, double max);

    List<Employee> getEmployeesByNameAndDepartmentName(String name, String department);

    List<Employee> getEmployeesByNameContaining(String keyword);


    Employee getEmployeesByName(String name);

    List<Employee> getEmployeesByJobTitle(String jobTitle);
}
