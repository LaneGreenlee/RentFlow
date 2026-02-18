package com.rentflow.service;

import com.rentflow.model.Payment;
import com.rentflow.model.PaymentStatus;
import com.rentflow.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Business Layer Service for Payment management
 * Provides all CRUD operations and business logic for Payment entities
 */
@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Create a new payment
     * 
     * @param payment Payment entity to create
     * @return Created payment with generated ID
     */
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    /**
     * Update an existing payment
     * 
     * @param payment Payment entity with updated data
     * @return Updated payment
     */
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    /**
     * Delete a payment by ID
     * 
     * @param id Payment ID to delete
     */
    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }

    /**
     * Find payment by ID
     * 
     * @param id Payment ID to find
     * @return Optional containing the payment if found
     */
    public Optional<Payment> findPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    /**
     * Get all payments
     * 
     * @return List of all payments
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Find payments by lease ID
     * 
     * @param leaseId Lease ID to search for
     * @return List of payments for the specified lease
     */
    public List<Payment> findPaymentsByLease(Integer leaseId) {
        return paymentRepository.findByLease_LeaseId(leaseId);
    }

    /**
     * Find payments by status
     * 
     * @param status Payment status (PENDING, COMPLETED, FAILED, etc.)
     * @return List of payments with the specified status
     */
    public List<Payment> findPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    /**
     * Find overdue payments
     * 
     * @return List of payments that are overdue
     */
    public List<Payment> findOverduePayments() {
        return paymentRepository.findOverduePayments(LocalDate.now());
    }

    /**
     * Find payments within a date range
     * 
     * @param startDate Start date of the range
     * @param endDate   End date of the range
     * @return List of payments within the specified date range
     */
    public List<Payment> findPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findPaymentsByDateRange(startDate, endDate);
    }

    /**
     * Calculate total payments for a lease
     * 
     * @param leaseId Lease ID to calculate total for
     * @return Total amount of all payments for the lease
     */
    public BigDecimal calculateTotalPaymentsForLease(Integer leaseId) {
        BigDecimal total = paymentRepository.getTotalPaymentsForLease(leaseId);
        return total != null ? total : BigDecimal.ZERO;
    }
}
