package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
