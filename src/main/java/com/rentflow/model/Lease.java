package com.rentflow.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leases")
public class Lease {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lease_id")
    private Integer leaseId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "monthly_rent", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyRent;
    
    @Column(name = "security_deposit", nullable = false, precision = 10, scale = 2)
    private BigDecimal securityDeposit;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "lease_status", nullable = false)
    private LeaseStatus leaseStatus;
    
    @Column(name = "lease_terms", columnDefinition = "TEXT")
    private String leaseTerms;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Lease() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.leaseStatus = LeaseStatus.PENDING;
    }
    
    public Lease(Property property, Tenant tenant, LocalDate startDate, LocalDate endDate,
                BigDecimal monthlyRent, BigDecimal securityDeposit) {
        this();
        this.property = property;
        this.tenant = tenant;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
        this.securityDeposit = securityDeposit;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getLeaseId() { return leaseId; }
    public void setLeaseId(Integer leaseId) { this.leaseId = leaseId; }
    
    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
    
    public Tenant getTenant() { return tenant; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public BigDecimal getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(BigDecimal monthlyRent) { this.monthlyRent = monthlyRent; }
    
    public BigDecimal getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(BigDecimal securityDeposit) { 
        this.securityDeposit = securityDeposit; 
    }
    
    public LeaseStatus getLeaseStatus() { return leaseStatus; }
    public void setLeaseStatus(LeaseStatus leaseStatus) { this.leaseStatus = leaseStatus; }
    
    public String getLeaseTerms() { return leaseTerms; }
    public void setLeaseTerms(String leaseTerms) { this.leaseTerms = leaseTerms; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isActive() {
        return leaseStatus == LeaseStatus.ACTIVE;
    }
    
    @Override
    public String toString() {
        return String.format("Lease[id=%d, property=%s, tenant=%s, status=%s, rent=%.2f]",
            leaseId, 
            property != null ? property.getAddress() : "N/A",
            tenant != null ? tenant.getFullName() : "N/A",
            leaseStatus, monthlyRent);
    }
}

