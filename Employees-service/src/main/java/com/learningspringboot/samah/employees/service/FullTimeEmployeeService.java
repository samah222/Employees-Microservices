package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.model.FullTimeEmployee;
import com.learningspringboot.samah.employees.repository.FullTimeEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FullTimeEmployeeService {
    public List<FullTimeEmployee> getEmployeesBySalaryRange(double min, double max);
}
