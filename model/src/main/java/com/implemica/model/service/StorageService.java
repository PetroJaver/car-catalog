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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

        s3Client.putObject(new PutObjectRequest(bucketName,fileName,file));

        file.delete();

        return fileName;
    }

    public byte[] downloadFile(String fileName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try{
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        }catch (IOException e){

        }
        return null;
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
