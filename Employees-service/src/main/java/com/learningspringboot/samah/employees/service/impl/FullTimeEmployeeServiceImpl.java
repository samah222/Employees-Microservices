package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.model.FullTimeEmployee;
import com.learningspringboot.samah.employees.repository.FullTimeEmployeeRepository;
import com.learningspringboot.samah.employees.service.FullTimeEmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullTimeEmployeeServiceImpl implements FullTimeEmployeeService {
    public FullTimeEmployeeServiceImpl(FullTimeEmployeeRepository repository) {
        this.repository = repository;
    }

    private FullTimeEmployeeRepository repository;
    public List<FullTimeEmployee> getEmployeesBySalaryRange(double min, double max){
        return repository.findBySalaryBetween(min, max);
    }

}
