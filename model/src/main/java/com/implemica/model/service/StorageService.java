package com.implemica.model.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("application.default.name")
    private String defaultImageName;

    @Value("application.storage-service.meta-data.image")
    private String metaDataForImages;

    @Autowired
    private AmazonS3 s3Client;

    public boolean deleteFile(String fileName) {
        if (!fileName.equals(defaultImageName)) {
            try {
                s3Client.deleteObject(bucketName, fileName);
            }catch (SdkClientException e){
                return false;
            }
        }

        return true;
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = String.format("%s.%s", UUID.randomUUID(), multipartFile.getOriginalFilename());
        File file = convertMultiPartFileToFile(multipartFile);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setCacheControl(metaDataForImages);

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            file.delete();
        } catch (Exception e) {
            fileName = defaultImageName;
        }

        return fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            convertedFile = null;
        }

        return convertedFile;
    }
}
