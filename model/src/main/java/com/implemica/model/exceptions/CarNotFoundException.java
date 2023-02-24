package com.implemica.model.exceptions;

/**
 * Exception that is thrown when a car is not found in the database.
 */
public class CarNotFoundException extends Exception {

    /**
     * Constructs a new CarNotFoundException with a custom message.
     *
     * @param message the detail message of the exception.
     */
    public CarNotFoundException(String message) {
        super(message);
    }
}
