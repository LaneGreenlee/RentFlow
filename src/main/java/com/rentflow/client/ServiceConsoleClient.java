package com.rentflow.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentflow.model.Property;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Simple console-based front end that invokes the REST services to test CRUD.
 *
 * How to run:
 * 1) Start the Spring Boot service (it must be listening on
 * http://localhost:8080).
 * 2) Run this main class. Optionally set RENTFLOW_BASE_URL to point at a hosted
 * service.
 */
public class ServiceConsoleClient {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final String baseUrl;

    public ServiceConsoleClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static void main(String[] args) throws Exception {
        String baseUrl = System.getenv().getOrDefault("RENTFLOW_BASE_URL", DEFAULT_BASE_URL);
        ServiceConsoleClient client = new ServiceConsoleClient(baseUrl);
        client.runPropertyCrudDemo();
    }

    private void runPropertyCrudDemo() throws Exception {
        System.out.println("Running Property CRUD demo against: " + baseUrl);

        String createJson = """
                {
                  "address": "123 Test St",
                  "city": "Bryan",
                  "state": "TX",
                  "zipCode": "77801",
                  "propertyType": "APARTMENT",
                  "bedrooms": 2,
                  "bathrooms": 1.5,
                  "squareFeet": 900,
                  "monthlyRent": 1200.00
                }
                """;

        HttpResponse<String> createResponse = post("/api/properties", createJson);
        System.out.println("Create status: " + createResponse.statusCode());
        Property created = objectMapper.readValue(createResponse.body(), Property.class);
        Integer propertyId = created.getPropertyId();
        System.out.println("Created property ID: " + propertyId);

        HttpResponse<String> getResponse = get("/api/properties/" + propertyId);
        System.out.println("Get status: " + getResponse.statusCode());
        System.out.println("Get response: " + getResponse.body());

        String updateJson = """
                {
                  "address": "123 Test St",
                  "city": "Bryan",
                  "state": "TX",
                  "zipCode": "77801",
                  "propertyType": "APARTMENT",
                  "bedrooms": 2,
                  "bathrooms": 1.5,
                  "squareFeet": 900,
                  "monthlyRent": 1350.00
                }
                """;

        HttpResponse<String> updateResponse = put("/api/properties/" + propertyId, updateJson);
        System.out.println("Update status: " + updateResponse.statusCode());
        System.out.println("Update response: " + updateResponse.body());

        HttpResponse<String> getAfterUpdate = get("/api/properties/" + propertyId);
        System.out.println("Get-after-update status: " + getAfterUpdate.statusCode());
        System.out.println("Get-after-update response: " + getAfterUpdate.body());

        HttpResponse<String> deleteResponse = delete("/api/properties/" + propertyId);
        System.out.println("Delete status: " + deleteResponse.statusCode());

        HttpResponse<String> getAfterDelete = get("/api/properties/" + propertyId);
        System.out.println("Get-after-delete status (expected 404): " + getAfterDelete.statusCode());
        System.out.println("Get-after-delete response: " + getAfterDelete.body());
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> put(String path, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> delete(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .DELETE()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
