package com.rentflow.service;

import com.rentflow.model.Property;
import com.rentflow.model.PropertyType;
import com.rentflow.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Business Layer Service for Property management
 * Provides all CRUD operations and business logic for Property entities
 */
@Service
@Transactional
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    /**
     * Create a new property
     * 
     * @param property Property entity to create
     * @return Created property with generated ID
     */
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    /**
     * Update an existing property
     * 
     * @param property Property entity with updated data
     * @return Updated property
     */
    public Property updateProperty(Property property) {
        return propertyRepository.save(property);
    }

    /**
     * Delete a property by ID
     * 
     * @param id Property ID to delete
     */
    public void deleteProperty(Integer id) {
        propertyRepository.deleteById(id);
    }

    /**
     * Find property by ID
     * 
     * @param id Property ID to find
     * @return Optional containing the property if found
     */
    public Optional<Property> findPropertyById(Integer id) {
        return propertyRepository.findById(id);
    }

    /**
     * Get all properties
     * 
     * @return List of all properties
     */
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    /**
     * Find properties by type
     * 
     * @param type Property type (APARTMENT, HOUSE, CONDO, etc.)
     * @return List of properties of the specified type
     */
    public List<Property> findPropertiesByType(PropertyType type) {
        return propertyRepository.findByPropertyType(type);
    }

    /**
     * Find properties within a rent range
     * 
     * @param minRent Minimum monthly rent
     * @param maxRent Maximum monthly rent
     * @return List of properties within the specified rent range
     */
    public List<Property> findPropertiesByRentRange(BigDecimal minRent, BigDecimal maxRent) {
        return propertyRepository.findByMonthlyRentBetween(minRent, maxRent);
    }
}
