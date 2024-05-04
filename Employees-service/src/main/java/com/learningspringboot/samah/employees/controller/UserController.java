package com.learningspringboot.samah.employees.controller;

import com.learningspringboot.samah.employees.dto.UserDto;
import com.learningspringboot.samah.employees.model.User;
import com.learningspringboot.samah.employees.repository.UserRepository;
import com.learningspringboot.samah.employees.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users APIs", description = "All Users APIs")

@RequestMapping("user/v1")
@RestController
public class UserController {
    public UserController(UserService userService) {
        this.userService = userService;
    }
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id){
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getUserAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserDto> AddNewUser(@RequestBody @Validated User user){
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.CREATED);
    }
}
