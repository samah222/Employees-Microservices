package com.learningspringboot.samah.employees.controller;

import com.learningspringboot.samah.employees.dto.*;
import com.learningspringboot.samah.employees.service.MyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users APIs", description = "All Users APIs")

@RequestMapping("/v1/user")
@RestController
public class MyUserController {
    private MyUserService userService;

    public MyUserController(MyUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register User", description = "Register User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @PostMapping("/register")
    public ResponseEntity<MyUserDto> AddNewUser(@RequestBody @Validated UserRegistrationDto userRegistrationDto) {
        return new ResponseEntity<>(userService.addNewUser(userRegistrationDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Verify token", description = "Verify the token for a new registered User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/verifyRegistration")
    public ResponseEntity<CustomResponse> verifyUserRegistration(@RequestParam("userid") int userId
            , @RequestParam("token") String token) {
        return new ResponseEntity<>(userService.validateVerificationToken(token, userId), HttpStatus.OK);
    }

    @Operation(summary = "Send a new token", description = "Send a new token for a new registered User, due to delay or error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")})
    @GetMapping("/resendVerificationToken")
    public ResponseEntity<CustomResponse> resendVerificationToken(@RequestParam("email") String email) {
        return new ResponseEntity<>(userService.resendVerificationToken(email), HttpStatus.OK);
    }

    @Operation(summary = "Verify new token", description = "Verify a new token after resend request")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/newToken")
    public ResponseEntity<CustomResponse> verifyNewToken(@RequestParam("token") String token,
                                                         @RequestParam("userid") int userId) {
        return new ResponseEntity<>(userService.validateVerificationToken(token, userId), HttpStatus.OK); //check
    }

    @Operation(summary = "Change Password", description = "Change Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")})
    @PostMapping("/changePassword")
    public ResponseEntity<CustomResponse> changePassword(@Validated @RequestBody ChangePasswordDto changePasswordDto) {
        return new ResponseEntity<CustomResponse>(userService.changePassword(changePasswordDto), HttpStatus.OK);
    }

    @Operation(summary = "Request Reset Password", description = "Request Reset Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")})
    @PostMapping("/requestResetPassword")
    public ResponseEntity<CustomResponse> requestResetPassword(@Validated @RequestBody RequestResetPasswordDto requestResetPasswordDto) {
        return new ResponseEntity<CustomResponse>(userService.requestResetPassword(requestResetPasswordDto), HttpStatus.OK);
    }

    @Operation(summary = " Reset Password", description = " Reset Password")
    @ApiResponses(value = {@ApiResponse(responseCode = "201")})
    @PostMapping("/resetPassword")
    public ResponseEntity<CustomResponse> ResetPassword(@Validated @RequestBody ResetPasswordDto resetPasswordDto) {
        return new ResponseEntity<CustomResponse>(userService.resetPassword(resetPasswordDto), HttpStatus.OK);
    }

    @Operation(summary = "Get a User", description = "Get a User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @GetMapping("/{id}")
    public ResponseEntity<MyUserDto> getUser(@Parameter(description = "ID of User to be retrieved",
            required = true) @PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get all users", description = "Get all users")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @GetMapping
    public ResponseEntity<List<MyUserDto>> getAllUser() {
        return new ResponseEntity<List<MyUserDto>>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Delete a User", description = "Delete a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@Parameter(description = "ID of User to be deleted",
            required = true) @PathVariable int id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Edit a User", description = "Edit a User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @PutMapping("/{id}")
    public ResponseEntity<MyUserDto> updateUser(@Validated @RequestBody MyUserDto myUserDto,
                                                @Parameter(description = "ID of User to be updated",
                                                        required = true) @PathVariable int id) {
        userService.updateUser(id, myUserDto);
        return new ResponseEntity<>(userService.updateUser(id, myUserDto), HttpStatus.OK);
    }

}
