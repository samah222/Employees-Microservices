package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.model.Department;
import com.learningspringboot.samah.employees.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DepartmentService {
    Department getDepartmentById(Integer id);
    List<Department> getAllDepartments();
    Department addDepartment(Department department);
    void deleteDepartmentById(Integer id);

}
