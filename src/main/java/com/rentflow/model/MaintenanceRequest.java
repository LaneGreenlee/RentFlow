package com.rentflow.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_requests")
public class MaintenanceRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    
    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceStatus status;
    
    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;
    
    @Column(name = "actual_cost", precision = 10, scale = 2)
    private BigDecimal actualCost;
    
    @Column(name = "completion_date")
    private LocalDate completionDate;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public MaintenanceRequest() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = MaintenanceStatus.OPEN;
        this.priority = Priority.MEDIUM;
    }
    
    public MaintenanceRequest(Property property, Tenant tenant, LocalDate requestDate,
                            String description, Priority priority) {
        this();
        this.property = property;
        this.tenant = tenant;
        this.requestDate = requestDate;
        this.description = description;
        this.priority = priority;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getRequestId() { return requestId; }
    public void setRequestId(Integer requestId) { this.requestId = requestId; }
    
    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
    
    public Tenant getTenant() { return tenant; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    
    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }
    
    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }
    
    public BigDecimal getActualCost() { return actualCost; }
    public void setActualCost(BigDecimal actualCost) { this.actualCost = actualCost; }
    
    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { 
        this.completionDate = completionDate; 
    }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isOpen() {
        return status == MaintenanceStatus.OPEN;
    }
    
    public boolean isCompleted() {
        return status == MaintenanceStatus.COMPLETED;
    }
    
    @Override
    public String toString() {
        return String.format("MaintenanceRequest[id=%d, property=%s, priority=%s, status=%s]",
            requestId,
            property != null ? property.getAddress() : "N/A",
            priority, status);
    }
}

