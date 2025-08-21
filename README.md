# Employees APIs

Employees management project using Spring Boot

## Table of Contents

- [Project Overview](#project-overview)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Running the Application](#running-the-application)
- [Project Description](#project-description)
- [Project APIs](#project-APIs)

## Project Overview

In this project, we intend to create a basic employee management system using spring boot, MySQL and Thymeleaf
templates.
Alongside, with mail-service that consume the published mails from RabbitMQ.

## Getting Started

### Prerequisites

The project using Spring boot 3 and Java 17

### Running the application

To run the application (using terminal) with development profile:

    mvn clean install 
    cd .\target\
    java -jar .\employees-1.0.1.jar --spring.profiles.active=dev

To run RabbitMQ using docker, run the following command:

    docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
    
Then you can open the management GUI : 

    http://localhost:15672/
    

## Project Technologies

1. Spring Boot (Spring web, Spring Validation, Spring JPA, Spring Security)
2. MySQL
3. RabbitMQ
4. JavaMailSender
5. Thymeleaf
6. OpenAPI for documentation
7. Lombok
8. Docker and Docker-compose
9. Zipkin

## Project APIs

![screenshot](employees.png)
