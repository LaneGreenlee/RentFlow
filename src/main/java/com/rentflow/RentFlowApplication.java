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
 * Architecture:
 * - REST Layer: Controllers expose HTTP endpoints (e.g., /api/properties)
 * - Business Layer: Services contain core business logic
 * - Presentation Layer: ConsoleUIService handles CLI interaction
 * - Data Layer: Repositories provide database access
 * 
 * Hosting note (example platform: Render):
 * 1) Build command: mvn clean package
 * 2) Start command: java -jar target/rentflow-property-manager-1.0.0.jar
 * 3) Set env vars: SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME,
 * SPRING_DATASOURCE_PASSWORD
 * 4) Expose port 8080 (Render detects PORT; Spring Boot uses 8080 by default).
 * This hosts the REST services under /api/*.
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
