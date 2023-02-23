package com.implemica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

/**
 * A configuration class for Swagger documentation of Car catalog API. This class provides methods to generate
 * Swagger documentation for the API, including API information, security configuration, and endpoint selection.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Generates API information, including the API title, version, contact information, and description.
     *
     * @return an {@code ApiInfo} object containing the API information
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("With this api you can work with the car catalog server. " +
                        "In api there is a standard user who has read-only access, " +
                        "and there is an admin who has more functionality: delete a car, " +
                        "update cars, add. The api has 7 endpoints with which you can work with it. " +
                        "Follow the instructions of the endpoints to successfully work with the api.\n\n" +
                        "Api has 2 controllers for admin authorization and crud operations with car catalog.\n\n" +
                        "To read cars from the catalog no authorization is needed, and to add and edit is necessary.")
                .title("Car catalog")
                .version("1.0")
                .contact(new Contact("Petro Sliusarenko", "", "petslu23@gmail.com"))
                .build();
    }

    /**
     * Generates a Swagger {@code Docket} object, which is the primary interface for configuring Swagger.
     * This method configures the API information, security, and endpoint selection for Swagger.
     *
     * @return a {@code Docket} object for configuring Swagger documentation
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Configures an {@code ApiKey} object for Swagger security.
     *
     * @return an {@code ApiKey} object for Swagger security
     */
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    /**
     * Configures a {@code SecurityContext} object for Swagger security.
     *
     * @return a {@code SecurityContext} object for Swagger security
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    /**
     * Configures a list of {@code SecurityReference} objects for Swagger security.
     *
     * @return a list of {@code SecurityReference} objects for Swagger security
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
