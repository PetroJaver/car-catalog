package com.implemica.application.util.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.implemica.model.service.StorageService;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.NoMoreInteractions;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class StorageServiceTest {
    @Mock
    AmazonS3 s3Client;

    @InjectMocks
    StorageService storageService;

    @Value("${application.bucket.name}")
    private String bucketName;

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

        assertTrue(storageService.uploadFile(file)==null);

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

        assertTrue(storageService.uploadFile(file)==null);

        verifyNoInteractions(s3Client);
    }
}
