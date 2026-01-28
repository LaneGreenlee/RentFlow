package com.rentflow.repository;

import com.rentflow.model.MaintenanceRequest;
import com.rentflow.model.MaintenanceStatus;
import com.rentflow.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Integer> {
    
    // Find by status
    List<MaintenanceRequest> findByStatus(MaintenanceStatus status);
    
    // Find by priority
    List<MaintenanceRequest> findByPriority(Priority priority);
    
    // Find by property ID
    List<MaintenanceRequest> findByProperty_PropertyId(Integer propertyId);
    
    // Find by tenant ID
    List<MaintenanceRequest> findByTenant_TenantId(Integer tenantId);
    
    // Find open requests
    @Query("SELECT m FROM MaintenanceRequest m WHERE m.status = 'OPEN'")
    List<MaintenanceRequest> findAllOpenRequests();
    
    // Find urgent open requests
    @Query("SELECT m FROM MaintenanceRequest m WHERE m.status = 'OPEN' AND m.priority = 'URGENT'")
    List<MaintenanceRequest> findUrgentOpenRequests();
    
    // Find requests by status and priority
    List<MaintenanceRequest> findByStatusAndPriority(MaintenanceStatus status, Priority priority);
    
    // Get total estimated cost for open requests
    @Query("SELECT SUM(m.estimatedCost) FROM MaintenanceRequest m WHERE m.status = 'OPEN'")
    BigDecimal getTotalEstimatedCostForOpenRequests();
    
    // Get total actual cost for completed requests
    @Query("SELECT SUM(m.actualCost) FROM MaintenanceRequest m WHERE m.status = 'COMPLETED'")
    BigDecimal getTotalActualCostForCompletedRequests();
    
    // Count requests by status
    Long countByStatus(MaintenanceStatus status);

    // Fetch maintenance requests with property to avoid lazy initialization when accessed in UI
    @Query("SELECT m FROM MaintenanceRequest m JOIN FETCH m.property")
    List<MaintenanceRequest> findAllWithProperty();
}
