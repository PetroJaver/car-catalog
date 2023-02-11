package com.implemica.config;


import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains methods for creating beans working with Amazon library.
 */
@Configuration
public class AmazonS3Config {

    /**
     * Creates an AmazonS3 bean in the context of a spring application. To work with the Amazon S3 service.
     * To access the Amazon service, the method takes keys from the instance on which the application runs.
     * @return AmazonS3 bean
     */
    @Bean
    public AmazonS3 s3Client(){
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();
    }
}
