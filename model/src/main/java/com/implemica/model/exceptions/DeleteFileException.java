package com.implemica.model.exceptions;

/**
 * Exception thrown when an error occurs while deleting a file from a storage service.
 * Extends {@link StorageServiceException}.
 */
public class DeleteFileException extends StorageServiceException {
    /**
     * Constructs a new DeleteFileException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public DeleteFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
