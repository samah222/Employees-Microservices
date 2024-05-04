package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.dto.Mail;
import com.learningspringboot.samah.employees.exception.EmployeeNotFoundException;
import com.learningspringboot.samah.employees.model.Employee;
import com.learningspringboot.samah.employees.publisher.RabbitmqMailProducer;
import com.learningspringboot.samah.employees.repository.EmployeeRepository;
import com.learningspringboot.samah.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository empRepo;
    @Autowired
    private RabbitmqMailProducer producer;
    
    @Override
    public List<Employee> getAllEmployees(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return empRepo.findAll(pageable).getContent();
    }

    @Override
    public Employee addEmployee(Employee employee) {
        producer.sendJsonMessage(new Mail(employee.getEmail(),"Welcome "+employee.getEmployeeName(),
                "Welcome to our company !!"));
        return empRepo.save(employee);
    }

    @Override
    public Employee editEmployee(Employee employee) {
        if(!empRepo.findById(employee.getId()).isPresent())
            throw new EmployeeNotFoundException("Employee not found");
        return empRepo.save(employee);
    }

    @Override
    public Employee getEmployee(int id) {
        return empRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
//        return employee
//        if (employee.isPresent())
//            return employee.get();
//        throw new RuntimeException("This employee is not present");
    }

    @Override
    public void deleteEmployee(int Id) {
        empRepo.deleteById(Id);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String n) {
        return empRepo.findByDepartmentName(n);
    }

    @Override
    public List<Employee> getEmployeesByNameAndDepartmentName(String name, String department) {
        return empRepo.findByEmployeeNameAndDepartmentName(name, department);
    }

    @Override
    public List<Employee> getEmployeesByNameContaining(String keyword) {
        return empRepo.findByEmployeeNameContainingIgnoreCase(keyword);
    }

    @Override
    public Employee getEmployeesByName(String name) {
        return empRepo.findByEmployeeNameIgnoreCase(name);
    }

    @Override
    public List<Employee> getEmployeesByJobTitle(String jobTitle) {
        return empRepo.findByJobTitle(jobTitle);
    }
//    public double calculateTotalSalaryForAllEmployees() {
//        List<Employee> allEmployees = empRepo.findAll();
//        return allEmployees.stream()
//                .mapToDouble(Employee::getSalary)
//                .sum();
//    }

//    public double calculateTotalSalaryForDepartment(String department) {
//        List<Employee> employeesInDepartment = empRepo.findByDepartment(department);
//        return employeesInDepartment.stream()
//                .mapToDouble(Employee::getSalary)
//                .sum();
//    }

//    public double calculateAverageSalaryForAllEmployees() {
//        List<Employee> allEmployees = empRepo.findAll();
//        return allEmployees.stream()
//                .mapToDouble(Employee::getSalary)
//                .average()
//                .orElse(0.0);
//    }

//    public List<String> getEmployeesAboveSalaryThreshold(double salaryThreshold) {
//        List<Employee> employeesAboveThreshold = empRepo.findAll().stream()
//                .filter(employee -> employee.getSalary() > salaryThreshold)
//                .collect(Collectors.toList());
//
//        return employeesAboveThreshold.stream()
//                .map(Employee::getEmployeeName)
//                .collect(Collectors.toList());
//    }

//    public Employee getEmployeeWithHighestSalary() {
//        List<Employee> allEmployees = empRepo.findAll();
//        Optional<Employee> employeeWithMaxSalary = allEmployees.stream()
//                .max(Comparator.comparing(Employee::getSalary));
//        return employeeWithMaxSalary.orElse(null);
//    }

//    public Employee getEmployeeWithLowestSalary() {
//        List<Employee> allEmployees = empRepo.findAll();
//        Optional<Employee> employeeWithMinSalary = allEmployees.stream()
//                .min(Comparator.comparing(Employee::getSalary));
//        return employeeWithMinSalary.orElse(null);
//    }

//    public double getAverageSalaryByDepartment(String department) {
//        List<Employee> employeesInDepartment = empRepo.findByDepartment(department);
//        return employeesInDepartment.stream()
//                .mapToDouble(Employee::getSalary)
//                .average()
//                .orElse(0.0);
//    }


}
