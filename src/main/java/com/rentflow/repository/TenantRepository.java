package com.rentflow.repository;

import com.rentflow.model.EmploymentStatus;
import com.rentflow.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {
    
    // Find tenant by email
    Optional<Tenant> findByEmail(String email);
    
    // Find tenants by employment status
    List<Tenant> findByEmploymentStatus(EmploymentStatus status);
    
    // Find tenants by last name
    List<Tenant> findByLastName(String lastName);
    
    // Find tenants by first and last name
    Optional<Tenant> findByFirstNameAndLastName(String firstName, String lastName);
    
    // Search tenants by partial name match (case insensitive)
    @Query("SELECT t FROM Tenant t WHERE LOWER(t.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) " +
           "OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Tenant> searchByName(String name);
    
    // Find tenants by phone
    Optional<Tenant> findByPhone(String phone);
    
    // Count tenants by employment status
    Long countByEmploymentStatus(EmploymentStatus status);
}
