package com.example.bookcatalog.application.exceptions;

public class DuplicateISBNException extends RuntimeException{
    public DuplicateISBNException(String message){
        super(message);
    }
}
