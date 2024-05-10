package com.learningspringboot.samah.employees.model;

import com.learningspringboot.samah.employees.Util.EmployeeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Employee extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 50, message = "employee name should be between 2 and 50 characters")
    @Column(name = "name", nullable = false)
    @NotBlank
    private String employeeName;

    @NotBlank
    @Size(min = 9, max = 16)
    private String phone;

    private String jobTitle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id") //, nullable = false
    private Department department;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToMany
    @JoinTable(
            name = "projects_employees",
            joinColumns = {
                    @JoinColumn(name = "employee_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "project_id")
            }
    )
    private List<Project> projects;

    @OneToOne(mappedBy = "employee") //, cascade = CascadeType.ALL
    private MyUser user;

    @NotNull(message = "Employee type must be specified, FULL_TIME or PART_TIME")
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType;

}
