package com.rentflow.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "properties")
public class Property {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Integer propertyId;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false, length = 100)
    private String city;
    
    @Column(nullable = false, length = 50)
    private String state;
    
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;
    
    @Column(nullable = false)
    private Integer bedrooms;
    
    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal bathrooms;
    
    @Column(name = "square_feet")
    private Integer squareFeet;
    
    @Column(name = "monthly_rent", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyRent;
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "purchase_price", precision = 12, scale = 2)
    private BigDecimal purchasePrice;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Property() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Property(String address, String city, String state, String zipCode, 
                   PropertyType propertyType, Integer bedrooms, BigDecimal bathrooms,
                   Integer squareFeet, BigDecimal monthlyRent) {
        this();
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.propertyType = propertyType;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.squareFeet = squareFeet;
        this.monthlyRent = monthlyRent;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getPropertyId() { return propertyId; }
    public void setPropertyId(Integer propertyId) { this.propertyId = propertyId; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    
    public PropertyType getPropertyType() { return propertyType; }
    public void setPropertyType(PropertyType propertyType) { this.propertyType = propertyType; }
    
    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer bedrooms) { this.bedrooms = bedrooms; }
    
    public BigDecimal getBathrooms() { return bathrooms; }
    public void setBathrooms(BigDecimal bathrooms) { this.bathrooms = bathrooms; }
    
    public Integer getSquareFeet() { return squareFeet; }
    public void setSquareFeet(Integer squareFeet) { this.squareFeet = squareFeet; }
    
    public BigDecimal getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(BigDecimal monthlyRent) { this.monthlyRent = monthlyRent; }
    
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return String.format("Property[id=%d, address='%s', city='%s', type=%s, rent=%.2f]",
            propertyId, address, city, propertyType, monthlyRent);
    }
}

