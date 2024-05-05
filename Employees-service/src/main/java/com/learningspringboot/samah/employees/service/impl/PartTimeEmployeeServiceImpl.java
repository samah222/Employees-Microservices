package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.repository.PartTimeEmployeeRepository;
import com.learningspringboot.samah.employees.service.PartTimeEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class PartTimeEmployeeServiceImpl implements PartTimeEmployeeService {
    public PartTimeEmployeeServiceImpl(PartTimeEmployeeRepository repository) {
        this.repository = repository;
    }
    private PartTimeEmployeeRepository repository;


}
