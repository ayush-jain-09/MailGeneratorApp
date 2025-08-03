package com.mailGenerator.mailGenerator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for the Spring Boot application.
 * This class configures CORS to allow requests from all origins, methods, and headers
 * for the API endpoints. This is necessary for the frontend to communicate with the
 * backend during development.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configure CORS for all API endpoints.
        registry.addMapping("/api/**")
                .allowedOrigins("*") // Allows requests from any origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allows these HTTP methods
                .allowedHeaders("*"); // Allows all headers
    }
}
