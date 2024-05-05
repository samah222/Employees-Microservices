package com.learningspringboot.samah.employees.exception;

public class UserNotActivatedException extends RuntimeException{
    public UserNotActivatedException(String message){
        super(message);
    }
}
