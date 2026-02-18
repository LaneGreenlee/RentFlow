package com.rentflow.controller;

import com.rentflow.model.Lease;
import com.rentflow.model.LeaseStatus;
import com.rentflow.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Lease management services
 * Exposes HTTP endpoints that invoke the business layer (LeaseService)
 * 
 * Base URL: /api/leases
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseController {

    private final LeaseService leaseService;

    @Autowired
    public LeaseController(LeaseService leaseService) {
        this.leaseService = leaseService;
    }

    /**
     * Create a new lease
     * POST /api/leases
     * 
     * @param lease Lease data in request body
     * @return Created lease with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Lease> createLease(@RequestBody Lease lease) {
        Lease created = leaseService.createLease(lease);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update an existing lease
     * PUT /api/leases/{id}
     * 
     * @param id    Lease ID
     * @param lease Updated lease data
     * @return Updated lease with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lease> updateLease(@PathVariable Integer id, @RequestBody Lease lease) {
        lease.setLeaseId(id);
        Lease updated = leaseService.updateLease(lease);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a lease
     * DELETE /api/leases/{id}
     * 
     * @param id Lease ID to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLease(@PathVariable Integer id) {
        leaseService.deleteLease(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a lease by ID
     * GET /api/leases/{id}
     * 
     * @param id Lease ID
     * @return Lease data with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lease> getLeaseById(@PathVariable Integer id) {
        return leaseService.findLeaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all leases
     * GET /api/leases
     * 
     * @return List of all leases with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Lease>> getAllLeases() {
        List<Lease> leases = leaseService.getAllLeases();
        return ResponseEntity.ok(leases);
    }

    /**
     * Get leases by property ID
     * GET /api/leases/property/{propertyId}
     * 
     * @param propertyId Property ID
     * @return List of leases for the specified property
     */
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Lease>> getLeasesByProperty(@PathVariable Integer propertyId) {
        List<Lease> leases = leaseService.findLeasesByProperty(propertyId);
        return ResponseEntity.ok(leases);
    }

    /**
     * Get leases by tenant ID
     * GET /api/leases/tenant/{tenantId}
     * 
     * @param tenantId Tenant ID
     * @return List of leases for the specified tenant
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Lease>> getLeasesByTenant(@PathVariable Integer tenantId) {
        List<Lease> leases = leaseService.findLeasesByTenant(tenantId);
        return ResponseEntity.ok(leases);
    }

    /**
     * Get leases by status
     * GET /api/leases/status/{status}
     * 
     * @param status Lease status (ACTIVE, EXPIRED, TERMINATED, etc.)
     * @return List of leases with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Lease>> getLeasesByStatus(@PathVariable LeaseStatus status) {
        List<Lease> leases = leaseService.findLeasesByStatus(status);
        return ResponseEntity.ok(leases);
    }

    /**
     * Get active leases
     * GET /api/leases/active
     * 
     * @return List of active leases
     */
    @GetMapping("/active")
    public ResponseEntity<List<Lease>> getActiveLeases() {
        List<Lease> leases = leaseService.findActiveLeases();
        return ResponseEntity.ok(leases);
    }

    /**
     * Get leases expiring soon
     * GET /api/leases/expiring-soon?days=30
     * 
     * @param days Number of days to look ahead (default: 30)
     * @return List of leases expiring within the specified period
     */
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<Lease>> getLeasesExpiringSoon(@RequestParam(defaultValue = "30") int days) {
        List<Lease> leases = leaseService.findLeasesExpiringSoon(days);
        return ResponseEntity.ok(leases);
    }
}
