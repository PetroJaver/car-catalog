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
import java.util.Objects;
import java.util.UUID;

/**
 * The service contains logic for working with files on AWS S3 Bucket.
 */
@Service
public class SimpleStorageServiceAWS implements StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("#{ new Integer(\"${application.storage-service.max-height}\")}")
    private Integer maxImageHeight;

    @Value("#{ new Integer('${application.storage-service.max-width}')}")
    private Integer maxImageWidth;

    @Value("${application.default.image}")
    private String defaultImageName;

    @Value("${application.storage-service.meta-data.image}")
    private String metaDataForImages;

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
        File file = convertMultiPartFileToFile(multipartFile);

        try (InputStream inputStream = reduceImageSize(file)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setCacheControl(metaDataForImages);

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            fileName = defaultImageName;
        }

        if (file != null) {
            file.delete();
        }

        return fileName;
    }

    private FileInputStream reduceImageSize(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);

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

        if((finalHeight!=startingHeight)||(finalWidth!=startingWidth)) {
            BufferedImage bufferedImageOutput = new BufferedImage(finalWidth, finalHeight, image.getType());

            Graphics2D g2d = bufferedImageOutput.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, finalWidth, finalHeight, null);
            g2d.dispose();

            String formatName = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            ImageIO.write(bufferedImageOutput, formatName, file);
        }

        return new FileInputStream(file);
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
