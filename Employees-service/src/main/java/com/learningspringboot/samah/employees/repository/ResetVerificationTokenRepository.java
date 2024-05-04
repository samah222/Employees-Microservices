package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.ResetVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetVerificationTokenRepository extends JpaRepository<ResetVerificationToken, Long> {
    ResetVerificationToken findByToken(String token);
    ResetVerificationToken findByUserId(Long id);
}
