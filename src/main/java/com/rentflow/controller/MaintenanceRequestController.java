package com.rentflow.controller;

import com.rentflow.model.MaintenanceRequest;
import com.rentflow.model.MaintenanceStatus;
import com.rentflow.model.Priority;
import com.rentflow.service.MaintenanceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Maintenance Request management services
 * Exposes HTTP endpoints that invoke the business layer
 * (MaintenanceRequestService)
 * 
 * Base URL: /api/maintenance-requests
 */
@RestController
@RequestMapping("/api/maintenance-requests")
public class MaintenanceRequestController {

    private final MaintenanceRequestService maintenanceRequestService;

    @Autowired
    public MaintenanceRequestController(MaintenanceRequestService maintenanceRequestService) {
        this.maintenanceRequestService = maintenanceRequestService;
    }

    /**
     * Create a new maintenance request
     * POST /api/maintenance-requests
     * 
     * @param maintenanceRequest MaintenanceRequest data in request body
     * @return Created maintenance request with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<MaintenanceRequest> createMaintenanceRequest(
            @RequestBody MaintenanceRequest maintenanceRequest) {
        MaintenanceRequest created = maintenanceRequestService.createMaintenanceRequest(maintenanceRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update an existing maintenance request
     * PUT /api/maintenance-requests/{id}
     * 
     * @param id                 MaintenanceRequest ID
     * @param maintenanceRequest Updated maintenance request data
     * @return Updated maintenance request with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRequest> updateMaintenanceRequest(@PathVariable Integer id,
            @RequestBody MaintenanceRequest maintenanceRequest) {
        maintenanceRequest.setRequestId(id);
        MaintenanceRequest updated = maintenanceRequestService.updateMaintenanceRequest(maintenanceRequest);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a maintenance request
     * DELETE /api/maintenance-requests/{id}
     * 
     * @param id MaintenanceRequest ID to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRequest(@PathVariable Integer id) {
        maintenanceRequestService.deleteMaintenanceRequest(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a maintenance request by ID
     * GET /api/maintenance-requests/{id}
     * 
     * @param id MaintenanceRequest ID
     * @return MaintenanceRequest data with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRequest> getMaintenanceRequestById(@PathVariable Integer id) {
        return maintenanceRequestService.findMaintenanceRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all maintenance requests
     * GET /api/maintenance-requests
     * 
     * @return List of all maintenance requests with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<MaintenanceRequest>> getAllMaintenanceRequests() {
        List<MaintenanceRequest> requests = maintenanceRequestService.getAllMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Get maintenance requests by property ID
     * GET /api/maintenance-requests/property/{propertyId}
     * 
     * @param propertyId Property ID
     * @return List of maintenance requests for the specified property
     */
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<MaintenanceRequest>> getMaintenanceRequestsByProperty(@PathVariable Integer propertyId) {
        List<MaintenanceRequest> requests = maintenanceRequestService.findMaintenanceRequestsByProperty(propertyId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get maintenance requests by status
     * GET /api/maintenance-requests/status/{status}
     * 
     * @param status Maintenance status (PENDING, IN_PROGRESS, COMPLETED, etc.)
     * @return List of maintenance requests with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceRequest>> getMaintenanceRequestsByStatus(
            @PathVariable MaintenanceStatus status) {
        List<MaintenanceRequest> requests = maintenanceRequestService.findMaintenanceRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get maintenance requests by priority
     * GET /api/maintenance-requests/priority/{priority}
     * 
     * @param priority Priority level (LOW, MEDIUM, HIGH, URGENT)
     * @return List of maintenance requests with the specified priority
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<MaintenanceRequest>> getMaintenanceRequestsByPriority(@PathVariable Priority priority) {
        List<MaintenanceRequest> requests = maintenanceRequestService.findMaintenanceRequestsByPriority(priority);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get pending maintenance requests
     * GET /api/maintenance-requests/pending
     * 
     * @return List of pending maintenance requests
     */
    @GetMapping("/pending")
    public ResponseEntity<List<MaintenanceRequest>> getPendingMaintenanceRequests() {
        List<MaintenanceRequest> requests = maintenanceRequestService.findPendingMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Get urgent maintenance requests
     * GET /api/maintenance-requests/urgent
     * 
     * @return List of urgent maintenance requests
     */
    @GetMapping("/urgent")
    public ResponseEntity<List<MaintenanceRequest>> getUrgentMaintenanceRequests() {
        List<MaintenanceRequest> requests = maintenanceRequestService.findUrgentMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }
}
