package com.rentflow.controller;

import com.rentflow.model.EmploymentStatus;
import com.rentflow.model.Tenant;
import com.rentflow.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Tenant management services
 * Exposes HTTP endpoints that invoke the business layer (TenantService)
 * 
 * Base URL: /api/tenants
 */
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    /**
     * Create a new tenant
     * POST /api/tenants
     * 
     * @param tenant Tenant data in request body
     * @return Created tenant with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        Tenant created = tenantService.createTenant(tenant);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update an existing tenant
     * PUT /api/tenants/{id}
     * 
     * @param id     Tenant ID
     * @param tenant Updated tenant data
     * @return Updated tenant with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tenant> updateTenant(@PathVariable Integer id, @RequestBody Tenant tenant) {
        tenant.setTenantId(id);
        Tenant updated = tenantService.updateTenant(tenant);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a tenant
     * DELETE /api/tenants/{id}
     * 
     * @param id Tenant ID to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Integer id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a tenant by ID
     * GET /api/tenants/{id}
     * 
     * @param id Tenant ID
     * @return Tenant data with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Integer id) {
        return tenantService.findTenantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all tenants
     * GET /api/tenants
     * 
     * @return List of all tenants with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    /**
     * Get tenant by email
     * GET /api/tenants/email/{email}
     * 
     * @param email Tenant email address
     * @return Tenant data with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Tenant> getTenantByEmail(@PathVariable String email) {
        return tenantService.findTenantByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get tenant by phone
     * GET /api/tenants/phone/{phone}
     * 
     * @param phone Tenant phone number
     * @return Tenant data with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Tenant> getTenantByPhone(@PathVariable String phone) {
        return tenantService.findTenantByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get tenants by employment status
     * GET /api/tenants/employment/{status}
     * 
     * @param status Employment status (EMPLOYED, UNEMPLOYED, SELF_EMPLOYED, etc.)
     * @return List of tenants with the specified employment status
     */
    @GetMapping("/employment/{status}")
    public ResponseEntity<List<Tenant>> getTenantsByEmploymentStatus(@PathVariable EmploymentStatus status) {
        List<Tenant> tenants = tenantService.findTenantsByEmploymentStatus(status);
        return ResponseEntity.ok(tenants);
    }

    /**
     * Search tenants by name
     * GET /api/tenants/search?name=John
     * 
     * @param name Search term for tenant name
     * @return List of matching tenants
     */
    @GetMapping("/search")
    public ResponseEntity<List<Tenant>> searchTenantsByName(@RequestParam String name) {
        List<Tenant> tenants = tenantService.searchTenantsByName(name);
        return ResponseEntity.ok(tenants);
    }
}
