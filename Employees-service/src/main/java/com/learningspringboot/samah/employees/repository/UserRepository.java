package com.learningspringboot.samah.employees.repository;

import com.learningspringboot.samah.employees.model.MyUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<MyUser,Integer> {
    MyUser findByEmail(String em);
    @Modifying
    @Transactional
    @Query("update MyUser u set u.enabled = :en")
    public void updateAllEnabled(boolean en);
}
