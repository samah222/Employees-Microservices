package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.dto.UserDto;
import com.learningspringboot.samah.employees.exception.UserNotFoundException;
import com.learningspringboot.samah.employees.mapping.UserMapper;
import com.learningspringboot.samah.employees.model.User;
import com.learningspringboot.samah.employees.repository.UserRepository;
import com.learningspringboot.samah.employees.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepository repository;
    @Override
    public User getUser(int id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    @Override
    public List<User> getUserAllUsers() {
        return repository.findAll();
    }

    @Override
    public UserDto addNewUser(User user) {
        if(user==null
        || user.getRole()==null
        || user.getPassword()==null)
            throw new NullPointerException("Missing data");
       String token = UUID.randomUUID().toString();
        return UserMapper.toDto(repository.save(user));
    }
}
