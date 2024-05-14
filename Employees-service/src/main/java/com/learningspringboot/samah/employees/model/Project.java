package com.learningspringboot.samah.employees.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 30, message = "project name should be between 2 and 30 characters")
    @Column(name = "name", nullable = false)
    @NotBlank
    private String projectName;

    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees;

    public Project(int id, String name) {
        this.id = id;
        this.projectName = name;
    }

}
