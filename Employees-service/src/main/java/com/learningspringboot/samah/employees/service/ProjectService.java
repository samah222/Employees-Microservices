package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.dto.ProjectDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    ProjectDto addProject(ProjectDto projectDto);

    ProjectDto getProjectById(Integer id);

    List<ProjectDto> getAllProjects();

    ProjectDto updateProject(Integer id, ProjectDto projectDto);

    void deleteProject(Integer id);
}
