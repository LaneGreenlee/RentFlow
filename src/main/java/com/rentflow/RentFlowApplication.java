package com.rentflow;

import com.rentflow.service.ConsoleUIService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

/**
 * RentFlow Application - Spring Boot Entry Point
 * 
 * ============================================================
 * EMBEDDED TOMCAT WEB SERVER
 * ============================================================
 * Spring Boot includes an embedded Apache Tomcat web server.
 * When this application starts, Tomcat automatically:
 * 
 * 1) Initializes on port 8080 (configured in application.properties)
 * 2) Registers all REST Controllers (@RestController endpoints)
 * 3) Handles incoming HTTP requests
 * 4) Routes requests to appropriate Controller methods
 * 5) Returns JSON responses
 * 
 * This eliminates need for external Tomcat installation.
 * The entire application runs as a single executable JAR.
 * 
 * Request Flow:
 * HTTP Request → Tomcat (port 8080) → Spring DispatcherServlet
 * → Controller → Service → Repository → Database
 * → Response (JSON) → HTTP Response
 * 
 * ============================================================
 * ARCHITECTURE LAYERS
 * ============================================================
 * - REST Layer: Controllers expose HTTP endpoints (e.g., /api/properties)
 * - Business Layer: Services contain core business logic
 * - Presentation Layer: ConsoleUIService handles CLI interaction
 * - Data Layer: Repositories provide database access
 * 
 * ============================================================
 * DEPLOYMENT NOTES
 * ============================================================
 * To deploy on cloud platform (e.g., Render, Heroku, AWS):
 * 
 * 1) Build command: mvn clean package
 * Output: target/rentflow-property-manager-1.0.0.jar
 * 
 * 2) Start command: java -jar target/rentflow-property-manager-1.0.0.jar
 * This launches the embedded Tomcat server automatically
 * 
 * 3) Environment Variables (set on platform):
 * - SPRING_DATASOURCE_URL
 * - SPRING_DATASOURCE_USERNAME
 * - SPRING_DATASOURCE_PASSWORD
 * 
 * 4) Port Exposure: Cloud platforms usually set PORT env var.
 * Spring Boot defaults to 8080, which works on most platforms.
 * Tomcat listens on this port for HTTP traffic.
 * 
 * 5) API becomes accessible at: https://your-app.render.com/api/*
 * (or equivalent URL depending on platform)
 */
@SpringBootApplication
public class RentFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentFlowApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ConsoleUIService consoleUIService) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            consoleUIService.run(scanner);
        };
    }
}
