package com.rentflow.controller;

import com.rentflow.model.Property;
import com.rentflow.model.PropertyType;
import com.rentflow.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Property management services
 * Exposes HTTP endpoints that invoke the business layer (PropertyService)
 * 
 * Base URL: /api/properties
 */
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    /**
     * Create a new property
     * POST /api/properties
     * 
     * @param property Property data in request body
     * @return Created property with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        Property created = propertyService.createProperty(property);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update an existing property
     * PUT /api/properties/{id}
     * 
     * @param id       Property ID
     * @param property Updated property data
     * @return Updated property with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Integer id, @RequestBody Property property) {
        property.setPropertyId(id);
        Property updated = propertyService.updateProperty(property);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a property
     * DELETE /api/properties/{id}
     * 
     * @param id Property ID to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a property by ID
     * GET /api/properties/{id}
     * 
     * @param id Property ID
     * @return Property data with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Integer id) {
        return propertyService.findPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all properties
     * GET /api/properties
     * 
     * @return List of all properties with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    /**
     * Get properties by type
     * GET /api/properties/type/{type}
     * 
     * @param type Property type (APARTMENT, HOUSE, CONDO, etc.)
     * @return List of properties of the specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Property>> getPropertiesByType(@PathVariable PropertyType type) {
        List<Property> properties = propertyService.findPropertiesByType(type);
        return ResponseEntity.ok(properties);
    }

    /**
     * Get properties within a rent range
     * GET /api/properties/rent-range?min=1000&max=2000
     * 
     * @param minRent Minimum monthly rent
     * @param maxRent Maximum monthly rent
     * @return List of properties within the specified rent range
     */
    @GetMapping("/rent-range")
    public ResponseEntity<List<Property>> getPropertiesByRentRange(
            @RequestParam BigDecimal minRent,
            @RequestParam BigDecimal maxRent) {
        List<Property> properties = propertyService.findPropertiesByRentRange(minRent, maxRent);
        return ResponseEntity.ok(properties);
    }
}
