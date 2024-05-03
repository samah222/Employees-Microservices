package com.learningspringboot.samah.employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class EmployeesApplication {
	private static final Logger logger = LoggerFactory.getLogger(EmployeesApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}

}
