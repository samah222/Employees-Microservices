package com.learningspringboot.samah.employees.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Size(min = 2, max=50, message = "Department name should be between 2 and 50 characters")
    @Column(name = "name", nullable = false )
    @NotBlank
    private String departmentName;

}
