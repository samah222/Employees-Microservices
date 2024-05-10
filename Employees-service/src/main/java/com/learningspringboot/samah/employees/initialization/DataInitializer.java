package com.learningspringboot.samah.employees.initialization;

import com.learningspringboot.samah.employees.model.Department;
import com.learningspringboot.samah.employees.repository.DepartmentRepository;
import com.learningspringboot.samah.employees.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    Department department1 = new Department(1, "Sale", null);
    Department department2 = new Department(2, "IT", null);
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void initialize() {
        initializeDepartment();
        //initializeEmployee();
    }

//    private void initializeEmployee() {
//        Employee emp = new Employee(1,"Samah Mahdi",10000, "samah.mahdi@gmail.com",
//                "0555555550","General Manager",department1, LocalDateTime.now(),LocalDateTime.now(),
//                new Address("street1","city1","state1","postal code1","Country"),
//                null,null);
//        employeeRepository.save(emp);
//    }

    private void initializeDepartment() {
        departmentRepository.save(department1);
        departmentRepository.save(department2);
    }

}
