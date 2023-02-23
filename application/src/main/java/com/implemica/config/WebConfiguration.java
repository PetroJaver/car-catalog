package com.implemica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for configuring Cross-Origin Resource Sharing (CORS) in a Spring MVC application.
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * Configures Cross-Origin Resource Sharing (CORS) for all endpoints in the application.
     *
     * @param registry the CORS configuration registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
    }
}
