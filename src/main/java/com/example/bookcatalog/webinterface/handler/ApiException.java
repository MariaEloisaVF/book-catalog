package com.example.bookcatalog.webinterface.handler;

import com.example.bookcatalog.application.exceptions.DuplicateISBNException;
import com.example.bookcatalog.application.exceptions.BookNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class ApiException {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateISBNException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateISBNException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    private Map<String, Object> error(HttpStatus status, String message){
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
    }
}
