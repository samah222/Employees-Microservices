package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.dto.Mail;
import com.learningspringboot.samah.employees.exception.EmployeeNotFoundException;
import com.learningspringboot.samah.employees.model.Employee;
import com.learningspringboot.samah.employees.publisher.RabbitmqMailProducer;
import com.learningspringboot.samah.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class EmployeeServiceImpl implements EmployeeService{
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
        producer.sendJsonMessage(new Mail(employee.getEmail(),"Welcome "+employee.getName(),
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
    public Employee getEmployee(Long id) {
        return empRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
//        return employee
//        if (employee.isPresent())
//            return employee.get();
//        throw new RuntimeException("This employee is not present");
    }

    @Override
    public void deleteEmployee(Long Id) {
        empRepo.deleteById(Id);
    }

//    @Override
//    public List<Employee> getEmployeesByDepartment(String department) {
//        return empRepo.findByDepartment(department);
//    }

    @Override
    public List<Employee> getEmployeesBySalaryBetween(double min, double max) {
        return empRepo.findBySalaryBetween(min,max);
    }

//    @Override
//    public List<Employee> getEmployeesByNameAndDepartment(String name, String department) {
//        return empRepo.findByNameAndDepartment(name, department);
//    }

    @Override
    public List<Employee> getEmployeesByNameContaining(String keyword) {
        return empRepo.findByNameContaining(keyword);
    }

    @Override
    public Employee getEmployeesByName(String name) {
        return empRepo.findByName(name);
    }

    @Override
    public List<Employee> getEmployeesByJobTitle(String jobTitle) {
        return empRepo.findByJobTitle(jobTitle);
    }
    public double calculateTotalSalaryForAllEmployees() {
        List<Employee> allEmployees = empRepo.findAll();
        return allEmployees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
    }

//    public double calculateTotalSalaryForDepartment(String department) {
//        List<Employee> employeesInDepartment = empRepo.findByDepartment(department);
//        return employeesInDepartment.stream()
//                .mapToDouble(Employee::getSalary)
//                .sum();
//    }

    public double calculateAverageSalaryForAllEmployees() {
        List<Employee> allEmployees = empRepo.findAll();
        return allEmployees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    public List<String> getEmployeesAboveSalaryThreshold(double salaryThreshold) {
        List<Employee> employeesAboveThreshold = empRepo.findAll().stream()
                .filter(employee -> employee.getSalary() > salaryThreshold)
                .collect(Collectors.toList());

        return employeesAboveThreshold.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public Employee getEmployeeWithHighestSalary() {
        List<Employee> allEmployees = empRepo.findAll();
        Optional<Employee> employeeWithMaxSalary = allEmployees.stream()
                .max(Comparator.comparing(Employee::getSalary));
        return employeeWithMaxSalary.orElse(null);
    }

    public Employee getEmployeeWithLowestSalary() {
        List<Employee> allEmployees = empRepo.findAll();
        Optional<Employee> employeeWithMinSalary = allEmployees.stream()
                .min(Comparator.comparing(Employee::getSalary));
        return employeeWithMinSalary.orElse(null);
    }

//    public double getAverageSalaryByDepartment(String department) {
//        List<Employee> employeesInDepartment = empRepo.findByDepartment(department);
//        return employeesInDepartment.stream()
//                .mapToDouble(Employee::getSalary)
//                .average()
//                .orElse(0.0);
//    }


}
