package com.implemica.advice;

import com.implemica.controller.AuthenticationRestController;
import com.implemica.controller.CarsController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains global exception handlers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link MethodArgumentNotValidException} thrown in all controllers of the application, and return client
     * error code 400 with response body.
     *
     * @param ex whose message will be in response body.
     * @return response body.
     * @see HttpStatus
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    /**
     * Handles {@link AuthenticationException} thrown in {@link AuthenticationRestController},
     * and returns the client error code 403 "message" - "Invalid email/password combination!"
     *
     * @return body of the response.
     * @see AuthenticationException
     */
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleAuthenticationException() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid email/password combination!");

        return response;
    }
}
