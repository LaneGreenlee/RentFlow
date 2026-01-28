package com.rentflow.repository;

import com.rentflow.model.Payment;
import com.rentflow.model.PaymentStatus;
import com.rentflow.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    // Find payments by lease ID
    List<Payment> findByLease_LeaseId(Integer leaseId);
    
    // Find payments by status
    List<Payment> findByPaymentStatus(PaymentStatus status);
    
    // Find completed payments
    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = 'COMPLETED'")
    List<Payment> findAllCompletedPayments();
    
    // Find pending payments
    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = 'PENDING'")
    List<Payment> findAllPendingPayments();
    
    // Find late payments (payment date after due date)
    @Query("SELECT p FROM Payment p WHERE p.paymentDate > p.dueDate AND p.paymentStatus = 'COMPLETED'")
    List<Payment> findLatePayments();
    
    // Find overdue payments (due date passed, still pending)
    @Query("SELECT p FROM Payment p WHERE p.dueDate < ?1 AND p.paymentStatus = 'PENDING'")
    List<Payment> findOverduePayments(LocalDate currentDate);
    
    // Find payments by date range
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN ?1 AND ?2")
    List<Payment> findPaymentsByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Find payments by type
    List<Payment> findByPaymentType(PaymentType paymentType);
    
    // Get total payments for a lease
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.lease.leaseId = ?1 AND p.paymentStatus = 'COMPLETED'")
    BigDecimal getTotalPaymentsForLease(Integer leaseId);
    
    // Get total rent payments received
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentType = 'RENT' AND p.paymentStatus = 'COMPLETED'")
    BigDecimal getTotalRentReceived();
    
    // Get total rent expected (all rent due dates that have passed)
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentType = 'RENT' AND p.dueDate <= ?1")
    BigDecimal getTotalRentExpected(LocalDate currentDate);
    
    // Get outstanding balance for a lease
    @Query("SELECT COALESCE(SUM(CASE WHEN p.paymentStatus = 'PENDING' THEN p.amount ELSE 0 END), 0) " +
           "FROM Payment p WHERE p.lease.leaseId = ?1 AND p.dueDate < ?2")
    BigDecimal getOutstandingBalanceForLease(Integer leaseId, LocalDate currentDate);
    
    // Find all payments for a tenant (through their active lease)
    @Query("SELECT p FROM Payment p WHERE p.lease.tenant.tenantId = ?1")
    List<Payment> findPaymentsByTenantId(Integer tenantId);
    
    // Get payment summary for a property
    @Query("SELECT p FROM Payment p WHERE p.lease.property.propertyId = ?1")
    List<Payment> findPaymentsByPropertyId(Integer propertyId);
    
    // Count late payments
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate > p.dueDate AND p.paymentStatus = 'COMPLETED'")
    Long countLatePayments();
    
    // Get monthly rent collection for a given month/year
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentType = 'RENT' " +
           "AND p.paymentStatus = 'COMPLETED' AND YEAR(p.paymentDate) = ?1 AND MONTH(p.paymentDate) = ?2")
    BigDecimal getMonthlyRentCollection(Integer year, Integer month);

       // Fetch payments with lease and tenant to avoid lazy initialization outside session
       @Query("SELECT p FROM Payment p JOIN FETCH p.lease l JOIN FETCH l.tenant")
       List<Payment> findAllWithLeaseAndTenant();
}
