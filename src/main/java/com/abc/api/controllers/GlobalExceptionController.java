package com.abc.api.controllers;

import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.payload.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

//@RestControllerAdvice
public class GlobalExceptionController {

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<WebResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
//        WebResponse<Object> response = WebResponse.builder()
//                .status(false)
//                .message(HttpStatus.NOT_FOUND.toString())
//                .errors(e.getMessage())
//                .data(null)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<WebResponse<Object>> handleGlobalException(Exception e) {
//        WebResponse<Object> response = WebResponse.builder()
//                .status(false)
//                .message(HttpStatus.INTERNAL_SERVER_ERROR.toString())
//                .errors(e.getMessage())
//                .data(null)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<WebResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
//        List<String> errors = e.getBindingResult().getFieldErrors()
//                .stream()
//                .map(fieldError ->  fieldError.getField() + ": " + fieldError.getDefaultMessage())
//                .toList();
//
//        WebResponse<Object> response = WebResponse.builder()
//                .status(false)
//                .message("Validation error")
//                .data(errors)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }
}
