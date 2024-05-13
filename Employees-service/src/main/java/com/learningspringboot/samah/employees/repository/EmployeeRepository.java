package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.Employee;
import com.learningspringboot.samah.employees.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Integer>, JpaRepository<Employee, Integer> {
    public Optional<Employee> findByUser(MyUser user);

    public List<Employee> findByDepartmentName(String n);

    public List<Employee> findByEmployeeNameAndDepartmentName(String name, String department);

    public List<Employee> findByEmployeeNameContainingIgnoreCase(String keyword);

    public List<Employee> findByEmployeeNameStartsWithIgnoreCase(String keyword);

    public Employee findByEmployeeNameIgnoreCase(String name);

    public List<Employee> findByJobTitle(String jobTitle);


}
