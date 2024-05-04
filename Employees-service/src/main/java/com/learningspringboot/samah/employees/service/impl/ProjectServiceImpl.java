package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.repository.ProjectRepository;
import com.learningspringboot.samah.employees.service.ProjectService;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository repository;

    public ProjectServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }
}
