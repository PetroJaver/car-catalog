package com.implemica.security;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * This exception thrown when token invalid or expired.
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {
    /**
     * The HTTP status code associated with the exception.
     */
    private HttpStatus httpStatus;

    /**
     * Constructs a new JwtAuthenticationException with the specified error message and HTTP status code.
     *
     * @param msg        the error message to associate with the exception.
     * @param httpStatus the HTTP status code to associate with the exception.
     */
    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
