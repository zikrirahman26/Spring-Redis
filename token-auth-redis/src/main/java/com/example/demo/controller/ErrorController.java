package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.WebResponse;

@RestControllerAdvice
public class ErrorController {
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> errorMessage(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().errors(exception.getReason()).build());
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<WebResponse<String>> errorMessage(InternalServerError exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<WebResponse<String>> nullPointerException(NullPointerException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder().errors(exception.getMessage()).build());
    }
}
