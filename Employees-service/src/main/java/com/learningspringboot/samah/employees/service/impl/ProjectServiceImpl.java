package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.dto.ProjectDto;
import com.learningspringboot.samah.employees.exception.InvalidDataException;
import com.learningspringboot.samah.employees.exception.ProjectNotFoundException;
import com.learningspringboot.samah.employees.mapping.ProjectMapper;
import com.learningspringboot.samah.employees.model.Project;
import com.learningspringboot.samah.employees.repository.ProjectRepository;
import com.learningspringboot.samah.employees.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMapper mapper;

    @Override
    public ProjectDto addProject(ProjectDto projectDto) {
        if (projectDto.getProjectName().isBlank() || projectDto.getProjectName() == null)
            throw new InvalidDataException("Project name can not be null");
        if (projectDto.getProjectName().length() < 2 || projectDto.getProjectName().length() > 30)
            throw new InvalidDataException("Project name is not valid");
        Project project = mapper.dtoToProject(projectDto);
        Project savedProject = projectRepository.save(project);
        return mapper.projectToDto(savedProject);
    }

    @Override
    public ProjectDto getProjectById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        return mapper.projectToDto(project);
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(mapper::projectToDto).toList();
    }

    @Override
    public ProjectDto updateProject(Integer id, ProjectDto projectDto) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        Optional.ofNullable(projectDto.getProjectName()).ifPresent(
                name -> {
                    if (projectDto.getProjectName().isBlank() || projectDto.getProjectName() == null)
                        throw new InvalidDataException("Project name can not be null");
                    if (projectDto.getProjectName().length() < 2 || projectDto.getProjectName().length() > 30)
                        throw new InvalidDataException("Project name is not valid");
                }
        );

        Project updatedProject = projectRepository.save(existingProject);
        return mapper.projectToDto(updatedProject);
    }

    @Override
    public void deleteProject(Integer id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
    }
}
