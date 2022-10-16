package com.implemica.application;

import  org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.implemica")
@EnableJpaRepositories(basePackages = "com.implemica")
@EntityScan(basePackages = "com.implemica")
public class CarCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarCatalogApplication.class, args);
    }

}
