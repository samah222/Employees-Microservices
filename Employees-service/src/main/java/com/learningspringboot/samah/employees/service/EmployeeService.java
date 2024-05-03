package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    public List<Employee> getAllEmployees(int pageNumber, int pageSize);

    public Employee addEmployee(Employee employee);

    public Employee editEmployee(Employee employee);

    public Employee getEmployee(Long id);

    public void deleteEmployee(Long Id);

//    List<Employee> getEmployeesByDepartment(String department);

    List<Employee> getEmployeesBySalaryBetween(double min, double max);

//    List<Employee> getEmployeesByNameAndDepartment(String name, String department);

    List<Employee> getEmployeesByNameContaining(String keyword);


    Employee getEmployeesByName(String name);

    List<Employee> getEmployeesByJobTitle(String jobTitle);
}
