package com.ticketnest.user_service.Exception;

import com.ticketnest.user_service.dto.RegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle validation errors (@Valid failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RegisterResponse> handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new RegisterResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    // Handle illegal arguments (e.g., duplicate email)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RegisterResponse> handleIllegalArgumentException(IllegalArgumentException ex)
    {
        return new ResponseEntity<>(new RegisterResponse(ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    // Handle any other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RegisterResponse> handleException(Exception ex)
    {
        return new ResponseEntity<>(new RegisterResponse(ex.getMessage()),HttpStatus.BAD_REQUEST);
    }
}
