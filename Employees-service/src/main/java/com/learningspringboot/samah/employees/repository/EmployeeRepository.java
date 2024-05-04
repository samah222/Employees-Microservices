package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee,Integer>, JpaRepository<Employee, Integer> {
    public List<Employee> findByDepartmentName(String n);

    public List<Employee> findByEmployeeNameAndDepartmentName(String name, String department);

    public List<Employee> findByEmployeeNameContainingIgnoreCase(String keyword);
    public List<Employee> findByEmployeeNameStartsWithIgnoreCase(String keyword);

    public Employee findByEmployeeNameIgnoreCase(String name);

    public List<Employee> findByJobTitle(String jobTitle);


}
