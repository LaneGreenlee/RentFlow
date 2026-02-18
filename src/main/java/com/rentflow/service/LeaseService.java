package com.rentflow.service;

import com.rentflow.model.Lease;
import com.rentflow.model.LeaseStatus;
import com.rentflow.repository.LeaseRepository;
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

    @Autowired
    public LeaseService(LeaseRepository leaseRepository) {
        this.leaseRepository = leaseRepository;
    }

    /**
     * Create a new lease
     * 
     * @param lease Lease entity to create
     * @return Created lease with generated ID
     */
    public Lease createLease(Lease lease) {
        return leaseRepository.save(lease);
    }

    /**
     * Update an existing lease
     * 
     * @param lease Lease entity with updated data
     * @return Updated lease
     */
    public Lease updateLease(Lease lease) {
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
