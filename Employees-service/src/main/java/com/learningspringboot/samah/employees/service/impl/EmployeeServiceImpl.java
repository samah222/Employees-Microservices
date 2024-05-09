package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.Util.EmployeeType;
import com.learningspringboot.samah.employees.dto.EmployeeDto;
import com.learningspringboot.samah.employees.dto.Mail;
import com.learningspringboot.samah.employees.exception.EmployeeNotFoundException;
import com.learningspringboot.samah.employees.exception.InvalidDataException;
import com.learningspringboot.samah.employees.mapping.EmployeeMapper;
import com.learningspringboot.samah.employees.model.Employee;
import com.learningspringboot.samah.employees.publisher.RabbitmqMailProducer;
import com.learningspringboot.samah.employees.repository.EmployeeRepository;
import com.learningspringboot.samah.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RabbitmqMailProducer producer;
    
    @Override
    public List<EmployeeDto> getAllEmployees(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAll(pageable)
                .getContent().stream()
                .map(EmployeeMapper::employeeToEmployeeDto).toList();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee addEmployee(Employee employee) {
        if (employee.getEmployeeName() == null
                || employee.getEmployeeName().isEmpty()
                || employee.getPhone() == null
                || employee.getPhone().isEmpty()
                || employee.getEmployeeType() == null
                || employee.getEmployeeType().toString().isEmpty()){
            throw new InvalidDataException("Employee data cannot be empty");
        }
        if ((employee.getEmployeeName().length() < 2 || (employee.getEmployeeName().length() > 50) )){
            throw new InvalidDataException("Employee name should be between 2 and 50 characters");
        }
        if(employee.getPhone().length()< 9 || employee.getPhone().length()> 16){
            throw new InvalidDataException("Employee phone should be between 9 and 16 numbers");
        }
        if(!EnumSet.allOf(EmployeeType.class).contains(employee.getEmployeeType()))
            throw new InvalidDataException("Invalid employee type");
        producer.sendJsonMessage(new Mail(employee.getMyUser().getEmail(),"Welcome "+employee.getEmployeeName(),
                "Welcome to our company !!"));
        return employeeRepository.save(employee);
    }

    @Override
    public Employee editEmployee(Employee employee) {

        Employee dbEmployee = employeeRepository.findById(employee.getId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        Optional.ofNullable(employee.getEmployeeName()).ifPresent(data -> {
            if(employee.getEmployeeName().isEmpty())
             throw new InvalidDataException("Employee name can not be empty");
            if ((employee.getEmployeeName().length() < 2 || (employee.getEmployeeName().length() > 50) ))
                throw new InvalidDataException("Employee name should be between 2 and 50 characters");
            dbEmployee.setEmployeeName(employee.getEmployeeName());
        });
        Optional.ofNullable(employee.getEmployeeType()).ifPresent(data -> {
            if(employee.getEmployeeType().toString().isEmpty())
             throw new InvalidDataException("Employee type can not be empty");
            if(!EnumSet.allOf(EmployeeType.class).contains(employee.getEmployeeType()))
                throw new InvalidDataException("Invalid employee type");
            dbEmployee.setEmployeeType(employee.getEmployeeType());
        });


        return employeeRepository.save(dbEmployee);
    }

    @Override
    public Employee getEmployee(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }

    @Override
    public void deleteEmployee(int Id) {
        employeeRepository.deleteById(Id);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String n) {
        return employeeRepository.findByDepartmentName(n);
    }

    @Override
    public List<Employee> getEmployeesByNameAndDepartmentName(String name, String department) {
        return employeeRepository.findByEmployeeNameAndDepartmentName(name, department);
    }

    @Override
    public List<Employee> getEmployeesByNameContaining(String keyword) {
        return employeeRepository.findByEmployeeNameContainingIgnoreCase(keyword);
    }

    @Override
    public Employee getEmployeesByName(String name) {
        return employeeRepository.findByEmployeeNameIgnoreCase(name);
    }

    @Override
    public List<Employee> getEmployeesByJobTitle(String jobTitle) {
        return employeeRepository.findByJobTitle(jobTitle);
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
