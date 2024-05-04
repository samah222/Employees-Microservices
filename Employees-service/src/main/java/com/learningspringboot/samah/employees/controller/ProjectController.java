package com.learningspringboot.samah.employees.controller;

import com.learningspringboot.samah.employees.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Projects APIs", description = "All Projects APIs")

@RequestMapping("project/v1")
@RestController
public class ProjectController {
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    private ProjectService projectService;

}
