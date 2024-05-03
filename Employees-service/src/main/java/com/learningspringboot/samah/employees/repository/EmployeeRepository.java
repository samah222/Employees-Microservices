package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee,Long>, JpaRepository<Employee, Long> {
//    public List<Employee> findByDepartment(String department);

    public List<Employee> findBySalaryBetween(double min, double max);

//    public List<Employee> findByNameAndDepartment(String name, String department);

    public List<Employee> findByNameContaining(String keyword);

    public Employee findByName(String name);

    public List<Employee> findByJobTitle(String jobTitle);

}
