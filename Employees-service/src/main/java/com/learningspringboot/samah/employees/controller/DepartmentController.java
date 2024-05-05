package com.learningspringboot.samah.employees.controller;

import com.learningspringboot.samah.employees.model.Department;
import com.learningspringboot.samah.employees.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Department APIs", description = "Department APIs")
@RequestMapping("/department")
@RestController
public class DepartmentController {
    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "Get department", description = "Get department")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Integer id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    @Operation(summary = "Get all department", description = "Get all department")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @Operation(summary = "Add department", description = "Add department")
    @ApiResponses(value = {@ApiResponse(responseCode = "201")})
    @PostMapping
    public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        Department addedDepartment = departmentService.addDepartment(department);
        return new ResponseEntity<>(addedDepartment, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete department", description = "Delete department")
    @ApiResponses(value = {@ApiResponse(responseCode = "204")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartmentById(@PathVariable Integer id) {
        departmentService.deleteDepartmentById(id);
        return ResponseEntity.noContent().build();
    }

}
