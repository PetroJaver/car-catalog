package com.implemica.model.service;

import com.implemica.model.exceptions.DeleteFileException;
import com.implemica.model.exceptions.UploadFileException;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is a specification of how a service layer should be implemented for working file storage logic.
 *
 * @see MultipartFile
 */
public interface StorageService {
    /**
     * Presents a contract method that must upload {@code multipartFile} in the storage.
     */
    String uploadFile(MultipartFile multipartFile) throws UploadFileException;

    /**
     * Presents a contract method that must delete {@code multipartFile} from the storage.
     */
    void deleteFile(String fileName) throws DeleteFileException;
}
