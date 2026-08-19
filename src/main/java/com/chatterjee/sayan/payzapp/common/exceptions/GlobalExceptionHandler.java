package com.chatterjee.sayan.payzapp.common.exceptions;

import com.chatterjee.sayan.payzapp.common.dtos.ErrorResponse;
import com.chatterjee.sayan.payzapp.common.dtos.FieldErrors;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateResourceException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(exception.getMessage(), exception.getErrorCode()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(exception.getMessage(), exception.getErrorCode()));
        String errorCode = exception.getResourceName().toUpperCase()+"NOT_FOUND";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(errorCode, exception.getMessage()));
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationException(BusinessRuleViolationException exception){
        String errorCode = exception.getErrorCode().toUpperCase()+"BAD_REQUEST";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(errorCode, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                               HttpServletRequest request){
        List<ErrorResponse> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(fe-> ErrorResponse.of(fe.getField(),fe.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of("Validation failed","Request validation failed"));

    }
}
