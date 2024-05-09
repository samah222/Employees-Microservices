//package com.learningspringboot.samah.employees.service;
//
//import com.learningspringboot.samah.employees.model.Employee;
//import com.learningspringboot.samah.employees.repository.EmployeeRepository;
//import com.learningspringboot.samah.employees.service.impl.EmployeeServiceImpl;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class EmployeeServiceImplTest {
//
//    @Mock
//    private EmployeeRepository employeeRepository;
//
//    @InjectMocks
//    private EmployeeServiceImpl employeeService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllEmployees() {
//        when(employeeRepository.findAll()).thenReturn(Arrays.asList(new Employee(), new Employee()));
//
//        List<Employee> employees = employeeService.getAllEmployees(0, 10);
//
//        assertEquals(2, employees.size());
//        verify(employeeRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testAddEmployee() {
//        Employee newEmployee = new Employee().builder().name("John Doe").salary(5000).build();
//        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);
//
//        Employee savedEmployee = employeeService.addEmployee(newEmployee);
//
//        //assertTrue(savedEmployee);
//        assertEquals("John Doe", savedEmployee.getName());
//        verify(employeeRepository, times(1)).save(newEmployee);
//    }
//
//    @Test
//    void testEditEmployee() {
//        Employee existingEmployee = new Employee().builder().Id(1L).name("Alice").salary(60000).build(); // Employee(1L, "Alice", 60000);
//        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);
//
//        Employee updatedEmployee = employeeService.editEmployee(existingEmployee);
//
//        assertEquals("Alice", updatedEmployee.getName());
//        verify(employeeRepository, times(1)).save(existingEmployee);
//    }
//
//    // Other test methods for remaining service methods...
//
//    // Test calculateTotalSalaryForAllEmployees method
//    @Test
//    void testCalculateTotalSalaryForAllEmployees() {
//        List<Employee> employees = Arrays.asList(
//                new Employee().builder().name("John").salary(50000).build(), //"John", 50000
//                new Employee().builder().name("Alice").salary(60000).build() //"Alice", 60000
//        );
//        when(employeeRepository.findAll()).thenReturn(employees);
//
//        double totalSalary = employeeService.calculateTotalSalaryForAllEmployees();
//
//        assertEquals(110000, totalSalary);
//        verify(employeeRepository, times(1)).findAll();
//    }
//
//    // Test other salary-related methods...
//
//    // Test any other methods as per your requirement
//}
