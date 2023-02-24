package com.implemica.model.exceptions;

/**
 * This exception is thrown when an error occurs while using a storage service.
 */
public class StorageServiceException extends Exception {
    /**
     * Constructs a new storage service exception with the specified detail message and cause.
     *
     * @param message the detail message of the exception.
     * @param cause   the cause of the exception.
     */
    public StorageServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
