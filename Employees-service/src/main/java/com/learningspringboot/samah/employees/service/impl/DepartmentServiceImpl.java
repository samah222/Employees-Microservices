package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.exception.InvalidDataException;
import com.learningspringboot.samah.employees.model.Department;
import com.learningspringboot.samah.employees.repository.DepartmentRepository;
import com.learningspringboot.samah.employees.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department addDepartment(Department department) {
        if (department.getName() == null || department.getName().isBlank()) {
            throw new InvalidDataException("Department name cannot be empty");
        }
        if (department.getName().length() < 2 || department.getName().length() > 50) {
            throw new InvalidDataException("Department name should be between 2 and 50 characters");
        }
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartmentById(Integer id) {
        departmentRepository.deleteById(id);
    }
}
