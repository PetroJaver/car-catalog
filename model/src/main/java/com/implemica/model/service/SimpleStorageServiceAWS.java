package com.implemica.model.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
     * The name of the default image that will be used if the uploaded file is not saved for some reason.
     */
    @Value("${application.default.image}")
    private String defaultImageName;

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
     * Delete the file from the AWS S3 Bucket. If {@code fileName} equals {@link #defaultImageName} file will be not
     * deleted, and method return {@code true}.
     *
     * @param fileName which will be removed from S3 Bucket.
     * @return false, if file can not delete.
     */
    public boolean deleteFile(String fileName) {
        if (!fileName.equals(defaultImageName)) {
            try {
                s3Client.deleteObject(bucketName, fileName);
            } catch (SdkClientException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts {@link MultipartFile} to {@link File}, and upload in the S3 bucket.
     * If the file is not uploaded for some reason, it will return the {@link #defaultImageName}.
     *
     * @param multipartFile will be converted and upload in the S3 bucket.
     * @return the name of the uploaded file in the S3 bucket.
     */
    public String uploadFile(MultipartFile multipartFile) {
        String fileName = String.format("%s.%s", UUID.randomUUID(), multipartFile.getOriginalFilename());

        try (InputStream inputStream = reduceImageSize(multipartFile)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setCacheControl(metaDataForImages);

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            fileName = defaultImageName;
        }

        return fileName;
    }

    /**
     * This method reduces the size of an image if its height or width exceeds the maximum defined in the
     * application properties file. It returns a FileInputStream of the reduced image.
     *
     * @param multipartFile The file to be reduced in size.
     * @return A FileInputStream of the reduced image.
     * @throws IOException If the image cannot be read or written.
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
