package com.learningspringboot.samah.employees.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "part_time_employees")

public class PartTimeEmployee {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "part_time_employee_id")
        private Integer id;

        @OneToOne
        @JoinColumn(name = "employee_id")
        private Employee employee;

        @Column(name = "hourly_rate")
        private Double hourlyRate;

        @Column(name = "hours_worked_per_week")
        private Integer hoursWorkedPerWeek;
}
