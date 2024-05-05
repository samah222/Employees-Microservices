package com.learningspringboot.samah.employees.exception;

public class InvalidUserDataException extends RuntimeException{
    public InvalidUserDataException(String message){
        super(message);
    }
}
