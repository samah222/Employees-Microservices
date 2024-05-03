package com.learningspringboot.samah.employees.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Size(min = 2, max=50, message = "name should be between 2 and 50 characters")
    @Column(name = "name", nullable = false )
    @NotBlank
    private String name;

    @Min(1000)
    private double salary;

    @Email
    @NotBlank
    //@Column(unique = true)
    private String email;

    //@Pattern(regexp ="^\\+[0-9]+$")
    @NotBlank
    @Size(min = 9, max=16)
    private String phone;

    private String jobTitle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Department department;
    
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name="updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Embedded
    private Address address;

}
