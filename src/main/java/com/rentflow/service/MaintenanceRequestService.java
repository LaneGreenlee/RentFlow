package com.rentflow.service;

import com.rentflow.model.MaintenanceRequest;
import com.rentflow.model.MaintenanceStatus;
import com.rentflow.model.Priority;
import com.rentflow.model.Property;
import com.rentflow.model.Tenant;
import com.rentflow.repository.MaintenanceRequestRepository;
import com.rentflow.repository.PropertyRepository;
import com.rentflow.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Business Layer Service for Maintenance Request management
 * Provides all CRUD operations and business logic for MaintenanceRequest
 * entities
 */
@Service
@Transactional
public class MaintenanceRequestService {

    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public MaintenanceRequestService(MaintenanceRequestRepository maintenanceRequestRepository,
            PropertyRepository propertyRepository,
            TenantRepository tenantRepository) {
        this.maintenanceRequestRepository = maintenanceRequestRepository;
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
    }

    /**
     * Create a new maintenance request
     * 
     * @param maintenanceRequest MaintenanceRequest entity to create
     * @return Created maintenance request with generated ID
     */
    public MaintenanceRequest createMaintenanceRequest(MaintenanceRequest maintenanceRequest) {
        // Fetch and set Property entity if only ID is provided
        if (maintenanceRequest.getProperty() != null && maintenanceRequest.getProperty().getPropertyId() != null) {
            Property property = propertyRepository.findById(maintenanceRequest.getProperty().getPropertyId())
                    .orElseThrow(() -> new RuntimeException(
                            "Property not found with id: " + maintenanceRequest.getProperty().getPropertyId()));
            maintenanceRequest.setProperty(property);
        }

        // Fetch and set Tenant entity if only ID is provided (tenant is optional)
        if (maintenanceRequest.getTenant() != null && maintenanceRequest.getTenant().getTenantId() != null) {
            Tenant tenant = tenantRepository.findById(maintenanceRequest.getTenant().getTenantId())
                    .orElseThrow(() -> new RuntimeException(
                            "Tenant not found with id: " + maintenanceRequest.getTenant().getTenantId()));
            maintenanceRequest.setTenant(tenant);
        }

        return maintenanceRequestRepository.save(maintenanceRequest);
    }

    /**
     * Update an existing maintenance request
     * 
     * @param maintenanceRequest MaintenanceRequest entity with updated data
     * @return Updated maintenance request
     */
    public MaintenanceRequest updateMaintenanceRequest(MaintenanceRequest maintenanceRequest) {
        // Fetch and set Property entity if only ID is provided
        if (maintenanceRequest.getProperty() != null && maintenanceRequest.getProperty().getPropertyId() != null) {
            Property property = propertyRepository.findById(maintenanceRequest.getProperty().getPropertyId())
                    .orElseThrow(() -> new RuntimeException(
                            "Property not found with id: " + maintenanceRequest.getProperty().getPropertyId()));
            maintenanceRequest.setProperty(property);
        }

        // Fetch and set Tenant entity if only ID is provided (tenant is optional)
        if (maintenanceRequest.getTenant() != null && maintenanceRequest.getTenant().getTenantId() != null) {
            Tenant tenant = tenantRepository.findById(maintenanceRequest.getTenant().getTenantId())
                    .orElseThrow(() -> new RuntimeException(
                            "Tenant not found with id: " + maintenanceRequest.getTenant().getTenantId()));
            maintenanceRequest.setTenant(tenant);
        }

        return maintenanceRequestRepository.save(maintenanceRequest);
    }

    /**
     * Delete a maintenance request by ID
     * 
     * @param id MaintenanceRequest ID to delete
     */
    public void deleteMaintenanceRequest(Integer id) {
        maintenanceRequestRepository.deleteById(id);
    }

    /**
     * Find maintenance request by ID
     * 
     * @param id MaintenanceRequest ID to find
     * @return Optional containing the maintenance request if found
     */
    public Optional<MaintenanceRequest> findMaintenanceRequestById(Integer id) {
        return maintenanceRequestRepository.findById(id);
    }

    /**
     * Get all maintenance requests
     * 
     * @return List of all maintenance requests
     */
    public List<MaintenanceRequest> getAllMaintenanceRequests() {
        return maintenanceRequestRepository.findAll();
    }

    /**
     * Find maintenance requests by property ID
     * 
     * @param propertyId Property ID to search for
     * @return List of maintenance requests for the specified property
     */
    public List<MaintenanceRequest> findMaintenanceRequestsByProperty(Integer propertyId) {
        return maintenanceRequestRepository.findByProperty_PropertyId(propertyId);
    }

    /**
     * Find maintenance requests by status
     * 
     * @param status Maintenance status (PENDING, IN_PROGRESS, COMPLETED, etc.)
     * @return List of maintenance requests with the specified status
     */
    public List<MaintenanceRequest> findMaintenanceRequestsByStatus(MaintenanceStatus status) {
        return maintenanceRequestRepository.findByStatus(status);
    }

    /**
     * Find maintenance requests by priority
     * 
     * @param priority Priority level (LOW, MEDIUM, HIGH, URGENT)
     * @return List of maintenance requests with the specified priority
     */
    public List<MaintenanceRequest> findMaintenanceRequestsByPriority(Priority priority) {
        return maintenanceRequestRepository.findByPriority(priority);
    }

    /**
     * Find pending maintenance requests
     * 
     * @return List of pending maintenance requests
     */
    public List<MaintenanceRequest> findPendingMaintenanceRequests() {
        return maintenanceRequestRepository.findAllOpenRequests();
    }

    /**
     * Find urgent maintenance requests
     * 
     * @return List of urgent maintenance requests
     */
    public List<MaintenanceRequest> findUrgentMaintenanceRequests() {
        return maintenanceRequestRepository.findUrgentOpenRequests();
    }
}
