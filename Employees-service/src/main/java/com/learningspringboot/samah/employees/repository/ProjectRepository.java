package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
}
