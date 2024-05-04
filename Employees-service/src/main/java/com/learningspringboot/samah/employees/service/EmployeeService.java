package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.model.Employee;

import java.util.List;

public interface EmployeeService {
    public List<Employee> getAllEmployees(int pageNumber, int pageSize);

    public Employee addEmployee(Employee employee);

    public Employee editEmployee(Employee employee);

    public Employee getEmployee(int id);

    public void deleteEmployee(int Id);

    List<Employee> getEmployeesByDepartment(String department);

    //List<Employee> getEmployeesBySalaryBetween(double min, double max);

    List<Employee> getEmployeesByNameAndDepartmentName(String name, String department);

    List<Employee> getEmployeesByNameContaining(String keyword);


    Employee getEmployeesByName(String name);

    List<Employee> getEmployeesByJobTitle(String jobTitle);
}
