package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.ChangePasswordVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangePasswordVerificationTokenRepository extends JpaRepository<ChangePasswordVerificationToken, Long> {
    ChangePasswordVerificationToken findByToken(String token);
    ChangePasswordVerificationToken findByMyUserId(Long id);
}
