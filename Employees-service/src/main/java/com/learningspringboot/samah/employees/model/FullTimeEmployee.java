package com.learningspringboot.samah.employees.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "full_time_employees")
public class FullTimeEmployee {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "full_time_employee_id")
        private Integer id;
        @OneToOne
        @JoinColumn(name = "employee_id")
        private Employee employee;
        @Min(1000)
        private Double salary;

        private Double bonus;
}
