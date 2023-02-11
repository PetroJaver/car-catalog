package com.implemica.application.util.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

import static org.junit.Assert.*;

import com.implemica.model.service.SimpleStorageServiceAWS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class SimpleStorageServiceAWSTest {
    @Mock
    AmazonS3 s3Client;

    @InjectMocks
    SimpleStorageServiceAWS storageService;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.default.image}")
    private String defaultImage;


    private final String fileName = "image";

    @Test
    public void deleteFileNotExist(){
        doThrow(new AmazonServiceException("")).when(s3Client).deleteObject(bucketName,fileName);

        assertFalse(storageService.deleteFile(fileName));

        verify(s3Client,times(1)).deleteObject(bucketName,fileName);
        verifyNoMoreInteractions(s3Client);
    }

    @Test
    public void deleteFileExist(){
        doNothing().when(s3Client).deleteObject(bucketName,fileName);
        assertTrue(storageService.deleteFile(fileName));

        verify(s3Client,times(1)).deleteObject(bucketName,fileName);
        verifyNoMoreInteractions(s3Client);
    }

    @Test
    public void uploadFileSuccessful(){
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return "file";
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        when(s3Client.putObject(any())).thenReturn(new PutObjectResult());

        assertTrue(storageService.uploadFile(file).matches(".*\\.file"));

        verify(s3Client,times(1)).putObject(any());
        verifyNoMoreInteractions(s3Client);
    }

    @Test
    public void uploadFileThrowException(){
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return "file";
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        when(s3Client.putObject(any())).thenThrow(AmazonServiceException.class);

        assertTrue(storageService.uploadFile(file)==defaultImage);

        verify(s3Client,times(1)).putObject(any());
        verifyNoMoreInteractions(s3Client);
    }

    @Test
    public void convertMultipartFileReturnNull(){
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return "file";
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                throw new IOException();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };

        assertTrue(storageService.uploadFile(file)==defaultImage);

        verifyNoInteractions(s3Client);
    }
}
