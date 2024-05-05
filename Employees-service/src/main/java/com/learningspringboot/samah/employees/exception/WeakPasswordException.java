package com.learningspringboot.samah.employees.exception;

public class WeakPasswordException extends RuntimeException{
    public WeakPasswordException(String message){
        super(message);
    }

}
