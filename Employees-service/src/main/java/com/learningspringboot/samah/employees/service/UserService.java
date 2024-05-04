package com.learningspringboot.samah.employees.service;

import com.learningspringboot.samah.employees.dto.UserDto;
import com.learningspringboot.samah.employees.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
     User getUser(int id);

    List<User> getUserAllUsers();

    UserDto addNewUser(User user);
}
