package com.implemica.model.exceptions;

/**
 * Exception thrown when an error occurs while uploading a file from a storage service.
 * Extends {@link StorageServiceException}.
 */
public class UploadFileException extends StorageServiceException {
    /**
     * Constructs a new UploadFileException with the specified error message and cause.
     *
     * @param message the error message for this exception.
     * @param cause   the cause of this exception.
     */
    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
