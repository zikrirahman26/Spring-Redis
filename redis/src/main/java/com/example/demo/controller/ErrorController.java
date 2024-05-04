package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.MethodNotAllowed;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.WebResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorController {
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> cResponseMessage(ConstraintViolationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().errors(e.getMessage()).build());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<WebResponse<String>> nResponseMessage(NullPointerException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder().errors(e.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> rResponseMessage(ResponseStatusException e){
        return ResponseEntity.status(e.getStatusCode())
                .body(WebResponse.<String>builder().errors(e.getReason()).build());
    }

    @ExceptionHandler(MethodNotAllowed.class)
    public ResponseEntity<WebResponse<String>> nResponseMessage(MethodNotAllowed e){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(WebResponse.<String>builder().errors(e.getStatusText()).build());
    }

    // @ExceptionHandler(RedisSystemException.class)
    // public ResponseEntity<WebResponse<String>> redisMessage(RedisSystemException e){
    //     return ResponseEntity.status(HttpStatus.)
    //             .body(WebResponse.<String>builder().errors(e.getCause().getMessage()).build());
    // }
}
