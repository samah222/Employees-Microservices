package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.dto.ProjectDto;
import com.learningspringboot.samah.employees.exception.InvalidDataException;
import com.learningspringboot.samah.employees.mapping.ProjectMapper;
import com.learningspringboot.samah.employees.model.Project;
import com.learningspringboot.samah.employees.repository.ProjectRepository;
import com.learningspringboot.samah.employees.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper mapper;

    @InjectMocks
    private ProjectService projectService = new ProjectServiceImpl();
    private ProjectDto projectDto;
    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void when_addProject_with_validInput_return_Success() {
        projectDto = new ProjectDto(1, "Test Project");
        project = new Project();
        project.setId(1);
        project.setProjectName("Test Project");
        when(mapper.dtoToProject(any())).thenReturn(project);
        when(mapper.projectToDto(any())).thenReturn(projectDto);
        when(projectRepository.save(any())).thenReturn(project);
        //projectDto.setProjectName("Test Project");
        // Debug: Check if projectRepository is mocked correctly
        //System.out.println("Mock behavior: " + projectRepository);
        // Debug: Check if projectRepository.save() mock is correctly set up
        //System.out.println("Mock save behavior: " + projectRepository.save(any()));

        ProjectDto createdProject = projectService.addProject(projectDto);

        assertNotNull(createdProject);
        assertEquals(projectDto.getProjectName(), createdProject.getProjectName());
    }

    @Test
    void when_addProject_with_invalidInput_throw_exception() {
        projectDto = new ProjectDto(1, "T");
        project = new Project();
        project.setId(1);
        project.setProjectName("T");
        when(mapper.dtoToProject(any())).thenReturn(project);
        when(mapper.projectToDto(any())).thenReturn(projectDto);
        when(projectRepository.save(any())).thenReturn(project);
        //ProjectDto createdProject = projectService.addProject(projectDto);
        assertThrows(InvalidDataException.class, () -> projectService.addProject(projectDto));
    }


//    @Test
//    void when_getProjectById_should_return_successfully() {
//        int id = 1;
//        when(mapper.projectToDto(any())).thenReturn(projectDto);
//        when(projectRepository.findById(id)).thenReturn(Optional.of(new Project()));
//        ProjectDto retrievedProject = projectService.getProjectById(id);
//        assertNotNull(retrievedProject);
//        assertEquals(projectDto.getProjectName(), retrievedProject.getProjectName());
//    }

    @Test
    void when_getAllProjects_return_successful() {
        List<Project> mockProjects = Arrays.asList(
                new Project(1, "Project 1"),
                new Project(2, "Project 2")
        );
        when(projectRepository.findAll()).thenReturn(mockProjects);

        when(mapper.projectToDto(mockProjects.get(0))).thenReturn(new ProjectDto(1, "Project 1"));
        when(mapper.projectToDto(mockProjects.get(1))).thenReturn(new ProjectDto(2, "Project 2"));

        List<ProjectDto> allProjects = projectService.getAllProjects();

        assertEquals(2, allProjects.size());
        assertEquals("Project 1", allProjects.get(0).getProjectName());
        assertEquals("Project 2", allProjects.get(1).getProjectName());
    }

    @Test
    void updateProject() {
    }

    @Test
    void deleteProject() {
    }
}
