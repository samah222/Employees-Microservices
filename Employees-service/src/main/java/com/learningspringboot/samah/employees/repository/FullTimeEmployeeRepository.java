package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.FullTimeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FullTimeEmployeeRepository extends JpaRepository<FullTimeEmployee, Integer> {
    List<FullTimeEmployee> findBySalaryBetween(double min, double max);
}
