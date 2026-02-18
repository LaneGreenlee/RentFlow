package com.rentflow.service;

import com.rentflow.model.EmploymentStatus;
import com.rentflow.model.Tenant;
import com.rentflow.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Business Layer Service for Tenant management
 * Provides all CRUD operations and business logic for Tenant entities
 */
@Service
@Transactional
public class TenantService {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    /**
     * Create a new tenant
     * 
     * @param tenant Tenant entity to create
     * @return Created tenant with generated ID
     */
    public Tenant createTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    /**
     * Update an existing tenant
     * 
     * @param tenant Tenant entity with updated data
     * @return Updated tenant
     */
    public Tenant updateTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    /**
     * Delete a tenant by ID
     * 
     * @param id Tenant ID to delete
     */
    public void deleteTenant(Integer id) {
        tenantRepository.deleteById(id);
    }

    /**
     * Find tenant by ID
     * 
     * @param id Tenant ID to find
     * @return Optional containing the tenant if found
     */
    public Optional<Tenant> findTenantById(Integer id) {
        return tenantRepository.findById(id);
    }

    /**
     * Get all tenants
     * 
     * @return List of all tenants
     */
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    /**
     * Find tenant by email
     * 
     * @param email Tenant email address
     * @return Optional containing the tenant if found
     */
    public Optional<Tenant> findTenantByEmail(String email) {
        return tenantRepository.findByEmail(email);
    }

    /**
     * Find tenant by phone number
     * 
     * @param phone Tenant phone number
     * @return Optional containing the tenant if found
     */
    public Optional<Tenant> findTenantByPhone(String phone) {
        return tenantRepository.findByPhone(phone);
    }

    /**
     * Find tenants by employment status
     * 
     * @param status Employment status (EMPLOYED, UNEMPLOYED, SELF_EMPLOYED, etc.)
     * @return List of tenants with the specified employment status
     */
    public List<Tenant> findTenantsByEmploymentStatus(EmploymentStatus status) {
        return tenantRepository.findByEmploymentStatus(status);
    }

    /**
     * Search tenants by name (first or last name contains search term)
     * 
     * @param name Search term for tenant name
     * @return List of matching tenants
     */
    public List<Tenant> searchTenantsByName(String name) {
        return tenantRepository.searchByName(name);
    }
}
