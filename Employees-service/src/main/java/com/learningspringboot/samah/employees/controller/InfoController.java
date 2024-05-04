package com.learningspringboot.samah.employees.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Application Info APIs", description = "Application Info APIs")
@RequestMapping("/info")
@RestController
public class InfoController {
    @Value("${spring.application.name}")
    private String name;
    @Value("${application.version}")
    private String version;

    @Operation(summary = "Get all application info", description = "Get all application info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping
    public String getInfo(){
        return "This application is "+name+" and this version: "+version;
    }
}
