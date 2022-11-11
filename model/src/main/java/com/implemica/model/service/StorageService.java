package com.implemica.model.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
@Slf4j
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public boolean deleteFile(String fileName){
        try{
            s3Client.deleteObject(bucketName, fileName);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String uploadFile(MultipartFile multipartFile){
        String fileName = UUID.randomUUID().toString();
        fileName += "." + multipartFile.getOriginalFilename();

        File file = convertMultiPartFileToFile(multipartFile);

        FileInputStream fileInputStream;

        try {
            fileInputStream = new FileInputStream(file);
        }catch (FileNotFoundException e){
            fileInputStream = null;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCacheControl("max-age=31536000, must-revalidate");

        s3Client.putObject(new PutObjectRequest(bucketName,fileName, fileInputStream,metadata));

        file.delete();

        return fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }catch (IOException e){

        }

        return convertedFile;
    }
}
