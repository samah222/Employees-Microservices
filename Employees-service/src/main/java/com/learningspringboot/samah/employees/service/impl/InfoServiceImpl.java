package com.learningspringboot.samah.employees.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class InfoServiceImpl {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${application.version}")
    private String applicationVersion;

    @Value("${application.server}")
    private String applicationServer;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private Environment environment;

    public String getAppNameAndVersion() {
        return "This application is " + applicationName + " and this version: " + applicationVersion;
    }

    public String getJavaVersion() {
        return environment.getProperty("JAVA.VERSION");
    }
}
