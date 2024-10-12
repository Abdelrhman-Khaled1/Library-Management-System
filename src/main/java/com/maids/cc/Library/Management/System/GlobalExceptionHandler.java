package com.maids.cc.Library.Management.System;

import com.maids.cc.Library.Management.System.Books.BookNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    public ResponseEntity<HashMap<String , List<String >>> handleBindException (BindException exception){
        List<String> errors = exception.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        HashMap<String ,List<String>> errMap = new HashMap<>();
        errMap.put("errors" , errors);
        return new ResponseEntity<>(errMap,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<HashMap<String, Object>> handleBookNotFoundException(BookNotFoundException ex) {
        logger.error("Error: {}", ex.getMessage());
        HashMap<String, Object> response = createErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HashMap<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Error: {}", ex.getMessage());
        HashMap<String, Object> response = createErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private HashMap<String, Object> createErrorResponse(HttpStatus status, String error, String message) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return response;
    }
}