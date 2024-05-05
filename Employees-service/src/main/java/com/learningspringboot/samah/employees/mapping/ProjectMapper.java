package com.learningspringboot.samah.employees.mapping;

import com.learningspringboot.samah.employees.dto.ProjectDto;
import com.learningspringboot.samah.employees.model.Project;

public class ProjectMapper {

    private ProjectMapper() {
        // Private constructor to prevent instantiation
    }

    public static ProjectDto projectToDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                // Map other fields as needed
                .build();
    }

    public static Project dtoToProject(ProjectDto projectDto) {
        return Project.builder()
                .id(projectDto.getId())
                .projectName(projectDto.getProjectName())
                // Map other fields as needed
                .build();
    }
}
