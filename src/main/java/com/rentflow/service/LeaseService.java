package com.rentflow.service;

import com.rentflow.model.Lease;
import com.rentflow.model.LeaseStatus;
import com.rentflow.model.Property;
import com.rentflow.model.Tenant;
import com.rentflow.repository.LeaseRepository;
import com.rentflow.repository.PropertyRepository;
import com.rentflow.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Business Layer Service for Lease management
 * Provides all CRUD operations and business logic for Lease entities
 */
@Service
@Transactional
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public LeaseService(LeaseRepository leaseRepository, PropertyRepository propertyRepository,
            TenantRepository tenantRepository) {
        this.leaseRepository = leaseRepository;
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
    }

    /**
     * Create a new lease
     * 
     * @param lease Lease entity to create
     * @return Created lease with generated ID
     */
    public Lease createLease(Lease lease) {
        // Fetch and set Property entity if only ID is provided
        if (lease.getProperty() != null && lease.getProperty().getPropertyId() != null) {
            Property property = propertyRepository.findById(lease.getProperty().getPropertyId())
                    .orElseThrow(() -> new RuntimeException(
                            "Property not found with id: " + lease.getProperty().getPropertyId()));
            lease.setProperty(property);
        }

        // Fetch and set Tenant entity if only ID is provided
        if (lease.getTenant() != null && lease.getTenant().getTenantId() != null) {
            Tenant tenant = tenantRepository.findById(lease.getTenant().getTenantId())
                    .orElseThrow(
                            () -> new RuntimeException("Tenant not found with id: " + lease.getTenant().getTenantId()));
            lease.setTenant(tenant);
        }

        return leaseRepository.save(lease);
    }

    /**
     * Update an existing lease
     * 
     * @param lease Lease entity with updated data
     * @return Updated lease
     */
    public Lease updateLease(Lease lease) {
        // Fetch and set Property entity if only ID is provided
        if (lease.getProperty() != null && lease.getProperty().getPropertyId() != null) {
            Property property = propertyRepository.findById(lease.getProperty().getPropertyId())
                    .orElseThrow(() -> new RuntimeException(
                            "Property not found with id: " + lease.getProperty().getPropertyId()));
            lease.setProperty(property);
        }

        // Fetch and set Tenant entity if only ID is provided
        if (lease.getTenant() != null && lease.getTenant().getTenantId() != null) {
            Tenant tenant = tenantRepository.findById(lease.getTenant().getTenantId())
                    .orElseThrow(
                            () -> new RuntimeException("Tenant not found with id: " + lease.getTenant().getTenantId()));
            lease.setTenant(tenant);
        }

        return leaseRepository.save(lease);
    }

    /**
     * Delete a lease by ID
     * 
     * @param id Lease ID to delete
     */
    public void deleteLease(Integer id) {
        leaseRepository.deleteById(id);
    }

    /**
     * Find lease by ID
     * 
     * @param id Lease ID to find
     * @return Optional containing the lease if found
     */
    public Optional<Lease> findLeaseById(Integer id) {
        return leaseRepository.findById(id);
    }

    /**
     * Get all leases
     * 
     * @return List of all leases
     */
    public List<Lease> getAllLeases() {
        return leaseRepository.findAll();
    }

    /**
     * Find leases by property ID
     * 
     * @param propertyId Property ID to search for
     * @return List of leases for the specified property
     */
    public List<Lease> findLeasesByProperty(Integer propertyId) {
        return leaseRepository.findByProperty_PropertyId(propertyId);
    }

    /**
     * Find leases by tenant ID
     * 
     * @param tenantId Tenant ID to search for
     * @return List of leases for the specified tenant
     */
    public List<Lease> findLeasesByTenant(Integer tenantId) {
        return leaseRepository.findByTenant_TenantId(tenantId);
    }

    /**
     * Find leases by status
     * 
     * @param status Lease status (ACTIVE, EXPIRED, TERMINATED, etc.)
     * @return List of leases with the specified status
     */
    public List<Lease> findLeasesByStatus(LeaseStatus status) {
        return leaseRepository.findByLeaseStatus(status);
    }

    /**
     * Find active leases (currently in effect)
     * 
     * @return List of active leases
     */
    public List<Lease> findActiveLeases() {
        return leaseRepository.findAllActiveLeases();
    }

    /**
     * Find leases expiring soon (within specified days)
     * 
     * @param days Number of days to look ahead
     * @return List of leases expiring within the specified period
     */
    public List<Lease> findLeasesExpiringSoon(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate futureDate = startDate.plusDays(days);
        return leaseRepository.findLeasesEndingSoon(startDate, futureDate);
    }
}
