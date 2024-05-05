package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.dto.ProjectDto;
import com.learningspringboot.samah.employees.exception.ProjectNotFoundException;
import com.learningspringboot.samah.employees.mapping.ProjectMapper;
import com.learningspringboot.samah.employees.model.Project;
import com.learningspringboot.samah.employees.repository.ProjectRepository;
import com.learningspringboot.samah.employees.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;

    @Override
    public ProjectDto addProject(ProjectDto projectDto) {
        Project project = ProjectMapper.dtoToProject(projectDto);
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.projectToDto(savedProject);
    }

    @Override
    public ProjectDto getProjectById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        return ProjectMapper.projectToDto(project);
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(ProjectMapper::projectToDto).collect(Collectors.toList());
    }

    @Override
    public ProjectDto updateProject(Integer id, ProjectDto projectDto) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));

        // Update existingProject fields based on the provided projectDto
        // You may want to add validation and mapping logic here

        Project updatedProject = projectRepository.save(existingProject);
        return ProjectMapper.projectToDto(updatedProject);
    }

    @Override
    public void deleteProjectById(Integer id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
    }
}
