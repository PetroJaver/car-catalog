package com.implemica.application;

import  org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {
        org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
})
@EnableSwagger2
@EnableCaching
@ComponentScan(basePackages = "com.implemica")
@EnableJpaRepositories(basePackages = "com.implemica")
@EntityScan(basePackages = "com.implemica")
public class CarCatalogApplication {
    //push on bitbucket
    public static void main(String[] args) {
        SpringApplication.run(CarCatalogApplication.class, args);
    }
}
