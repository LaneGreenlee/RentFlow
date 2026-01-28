package com.rentflow.repository;

import com.rentflow.model.Lease;
import com.rentflow.model.LeaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Integer> {
    
    // Find leases by status
    List<Lease> findByLeaseStatus(LeaseStatus status);
    
    // Find leases by property ID
    List<Lease> findByProperty_PropertyId(Integer propertyId);
    
    // Find leases by tenant ID
    List<Lease> findByTenant_TenantId(Integer tenantId);
    
    // Find active leases
    @Query("SELECT l FROM Lease l WHERE l.leaseStatus = 'ACTIVE'")
    List<Lease> findAllActiveLeases();

    // Find active leases and fetch associated property and tenant to avoid lazy initialization outside session
    @Query("SELECT l FROM Lease l JOIN FETCH l.property p JOIN FETCH l.tenant t WHERE l.leaseStatus = 'ACTIVE'")
    List<Lease> findAllActiveLeasesWithPropertyAndTenant();

    // Find all leases and fetch associated property and tenant
    @Query("SELECT l FROM Lease l JOIN FETCH l.property p JOIN FETCH l.tenant t")
    List<Lease> findAllWithPropertyAndTenant();
    
    // Find active lease for a property
    @Query("SELECT l FROM Lease l WHERE l.property.propertyId = ?1 AND l.leaseStatus = 'ACTIVE'")
    Optional<Lease> findActiveLeaseByProperty(Integer propertyId);
    
    // Find active lease for a tenant
    @Query("SELECT l FROM Lease l WHERE l.tenant.tenantId = ?1 AND l.leaseStatus = 'ACTIVE'")
    Optional<Lease> findActiveLeaseByTenant(Integer tenantId);
    
    // Find leases ending soon (within X days)
    @Query("SELECT l FROM Lease l WHERE l.leaseStatus = 'ACTIVE' AND l.endDate BETWEEN ?1 AND ?2")
    List<Lease> findLeasesEndingSoon(LocalDate startDate, LocalDate endDate);
    
    // Find expired leases that haven't been updated
    @Query("SELECT l FROM Lease l WHERE l.endDate < ?1 AND l.leaseStatus = 'ACTIVE'")
    List<Lease> findExpiredActiveLeases(LocalDate currentDate);
    
    // Count leases by status
    Long countByLeaseStatus(LeaseStatus status);
}