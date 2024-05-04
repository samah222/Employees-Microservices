package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.PartTimeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartTimeEmployeeRepository extends JpaRepository<PartTimeEmployee, Integer> {
}
