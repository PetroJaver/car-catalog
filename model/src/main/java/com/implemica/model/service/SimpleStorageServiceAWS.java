package com.implemica.model.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.implemica.model.exceptions.DeleteFileException;
import com.implemica.model.exceptions.UploadFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * The service contains logic for working with files on AWS S3 Bucket.
 */
@Service
public class SimpleStorageServiceAWS implements StorageService {
    /**
     * The name of the AWS S3 bucket.
     */
    @Value("${application.bucket.name}")
    private String bucketName;

    /**
     * The maximum height of an image that can be uploaded to the S3 bucket.
     */
    @Value("#{ new Integer(\"${application.storage-service.max-height}\")}")
    private Integer maxImageHeight;

    /**
     * The maximum width of an image that can be uploaded to the S3 bucket.
     */
    @Value("#{ new Integer('${application.storage-service.max-width}')}")
    private Integer maxImageWidth;

    /**
     * The metadata for images that will be uploaded to the S3 bucket.
     */
    @Value("${application.storage-service.meta-data.image}")
    private String metaDataForImages;

    /**
     * The AWS S3 client that is used to interact with the S3 bucket.
     */
    @Autowired
    private AmazonS3 s3Client;

    /**
     * Deletes a file from the AWS S3 bucket.
     *
     * @param fileName The name of the file to be deleted.
     * @throws DeleteFileException if the file was not deleted or if there is an issue with AmazonS3.
     */
    public void deleteFile(String fileName) throws DeleteFileException {
        try {
            s3Client.deleteObject(bucketName, fileName);
        } catch (SdkClientException e) {
            throw new DeleteFileException("File was not deleted from AWS S3 storage, something is wrong with AmazonS3.", e);
        }
    }

    /**
     * Uploads a file to the AWS S3 bucket.
     *
     * @param multipartFile The file to be uploaded.
     * @return The name of the file that was uploaded.
     * @throws UploadFileException if the file was not uploaded or if there is an issue with AmazonS3.
     */
    public String uploadFile(MultipartFile multipartFile) throws UploadFileException {
        String fileName = String.format("%s.%s", UUID.randomUUID(), multipartFile.getOriginalFilename());

        try (InputStream inputStream = reduceImageSize(multipartFile)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setCacheControl(metaDataForImages);

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (SdkClientException e) {
            throw new UploadFileException("File wasn't uploaded to AWS S3 storage, something is wrong with AmazonS3", e);
        } catch (IOException e) {
            throw new UploadFileException("File not read", e);
        }

        return fileName;
    }

    /**
     * Reduces the size of an image to fit within the maximum height and width for images.
     *
     * @param multipartFile the image to be resized.
     * @return An {@link InputStream} of the resized image.
     * @throws IOException if the multipartFile cannot be read or written.
     */
    private InputStream reduceImageSize(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));

        int startingHeight = image.getHeight();
        int startingWidth = image.getWidth();
        int finalHeight = startingHeight;
        int finalWidth = startingWidth;

        if (finalHeight > maxImageHeight) {
            finalWidth = (int) ((double) maxImageHeight / finalHeight * finalWidth);
            finalHeight = maxImageHeight;
        }

        if (finalWidth > maxImageWidth) {
            finalHeight = (int) ((double) maxImageWidth / finalWidth * finalHeight);
            finalWidth = maxImageWidth;
        }

        //check if the multipart file size has changed
        if ((finalHeight != startingHeight) || (finalWidth != startingWidth)) {
            BufferedImage resizedImage = new BufferedImage(finalWidth, finalHeight, image.getType());

            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, finalWidth, finalHeight, null);
            g2d.dispose();
            image = resizedImage;
        }

        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
