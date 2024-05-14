package com.learningspringboot.samah.employees.mapping;

import com.learningspringboot.samah.employees.dto.ProjectDto;
import com.learningspringboot.samah.employees.model.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Data
@Service
public class ProjectMapper {

    public ProjectDto projectToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setProjectName(project.getProjectName());
        dto.setId(project.getId());
        return dto;
    }

    public Project dtoToProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setId(projectDto.getId());
        return project;
    }
}
