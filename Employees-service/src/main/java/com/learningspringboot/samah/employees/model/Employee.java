package com.learningspringboot.samah.employees.model;

import com.learningspringboot.samah.employees.Util.EmployeeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class Employee  extends TrackingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Size(min = 2, max=50, message = "employee name should be between 2 and 50 characters")
    @Column(name = "name", nullable = false )
    @NotBlank
    private String employeeName;

    //@Pattern(regexp ="^\\+[0-9]+$")
    @NotBlank
    @Size(min = 9, max=16)
    private String phone;

    private String jobTitle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id") //, nullable = false
    @OnDelete(action = OnDeleteAction.CASCADE)
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
                    @JoinColumn(name = "employee_id") },
            inverseJoinColumns = {
                    @JoinColumn(name = "project_id")
            }
    )
    private List<Project> projects;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType ;

}
