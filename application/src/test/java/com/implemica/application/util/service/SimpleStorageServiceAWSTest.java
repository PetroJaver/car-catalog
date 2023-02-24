package com.implemica.application.util.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.implemica.model.exceptions.DeleteFileException;
import com.implemica.model.exceptions.UploadFileException;
import com.implemica.model.service.SimpleStorageServiceAWS;
import com.implemica.model.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.lang.reflect.Field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleStorageServiceAWSTest {
    @Mock
    private static AmazonS3 s3Client;

    @InjectMocks
    private static SimpleStorageServiceAWS storageService;

    @Value("${application.bucket.name}")
    private static String bucketName;

    @Value("${application.default.image}")
    private static String defaultImage;

    private static final String fileName = "image.png";

    public static void setUpValues() throws Exception {
        Class<? extends StorageService> simpleStorageServiceAWSClass = storageService.getClass();
        Field maxImageHeightField = simpleStorageServiceAWSClass.getDeclaredField("maxImageHeight");
        Field maxImageWidthField = simpleStorageServiceAWSClass.getDeclaredField("maxImageWidth");

        maxImageHeightField.setAccessible(true);
        maxImageWidthField.setAccessible(true);

        maxImageHeightField.set(storageService, 400);
        maxImageWidthField.set(storageService, 700);
    }


    @Test
    public void deleteFileNotExist() {
        doThrow(new AmazonServiceException("")).when(s3Client).deleteObject(bucketName, fileName);

        assertThrows("File was not deleted from AWS S3 storage, something is wrong with AmazonS3.", DeleteFileException.class, () -> storageService.deleteFile(fileName));

        verify(s3Client, times(1)).deleteObject(bucketName, fileName);
        verifyNoMoreInteractions(s3Client);
    }

    @Test
    public void deleteFileExist() throws Exception {
        doNothing().when(s3Client).deleteObject(bucketName, fileName);
        storageService.deleteFile(fileName);

        verify(s3Client, times(1)).deleteObject(bucketName, fileName);
        verifyNoMoreInteractions(s3Client);
    }

    @Test
    public void uploadFileSuccessful() throws Exception {
        setUpValues();
        MultipartFile file = new MockMultipartFile(fileName, fileName, "image/png",
                new FileInputStream("./src/test/resources/files/testCarImagePorshe911.png"));

        when(s3Client.putObject(any())).thenReturn(new PutObjectResult());

        assertTrue(storageService.uploadFile(file).matches(".*\\." + fileName));

        verify(s3Client, times(1)).putObject(any());
        verifyNoMoreInteractions(s3Client);
    }

    @Test
    public void uploadFileThrowException() throws Exception {
        setUpValues();
        MultipartFile file = new MockMultipartFile(fileName, fileName, "image/png",
                new FileInputStream("./src/test/resources/files/testCarImagePorshe911.png"));
        when(s3Client.putObject(any())).thenThrow(AmazonServiceException.class);

        assertThrows("File wasn't uploaded to AWS S3 storage, something is wrong with AmazonS3", UploadFileException.class, () -> storageService.uploadFile(file));

        verify(s3Client, times(1)).putObject(any());
        verifyNoMoreInteractions(s3Client);
    }
}
