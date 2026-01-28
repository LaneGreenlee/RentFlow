package com.rentflow.repository;

import com.rentflow.model.Property;
import com.rentflow.model.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    
    // Find properties by city
    List<Property> findByCity(String city);
    
    // Find properties by state
    List<Property> findByState(String state);
    
    // Find properties by type
    List<Property> findByPropertyType(PropertyType propertyType);
    
    // Find properties with rent less than or equal to max
    List<Property> findByMonthlyRentLessThanEqual(BigDecimal maxRent);
    
    // Find properties with rent between min and max
    List<Property> findByMonthlyRentBetween(BigDecimal minRent, BigDecimal maxRent);
    
    // Find properties by number of bedrooms
    List<Property> findByBedrooms(Integer bedrooms);
    
    // Find properties by city and type
    List<Property> findByCityAndPropertyType(String city, PropertyType type);
    
    // Custom query: Get total monthly rent income
    @Query("SELECT SUM(p.monthlyRent) FROM Property p")
    BigDecimal getTotalMonthlyRentIncome();
    
    // Custom query: Get average rent by city
    @Query("SELECT AVG(p.monthlyRent) FROM Property p WHERE p.city = ?1")
    BigDecimal getAverageRentByCity(String city);
    
    // Count properties by type
    Long countByPropertyType(PropertyType propertyType);
}
