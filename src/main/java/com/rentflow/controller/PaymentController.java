package com.rentflow.controller;

import com.rentflow.model.Payment;
import com.rentflow.model.PaymentStatus;
import com.rentflow.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Payment management services
 * Exposes HTTP endpoints that invoke the business layer (PaymentService)
 * 
 * Base URL: /api/payments
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Create a new payment
     * POST /api/payments
     * 
     * @param payment Payment data in request body
     * @return Created payment with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment created = paymentService.createPayment(payment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update an existing payment
     * PUT /api/payments/{id}
     * 
     * @param id      Payment ID
     * @param payment Updated payment data
     * @return Updated payment with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Integer id, @RequestBody Payment payment) {
        payment.setPaymentId(id);
        Payment updated = paymentService.updatePayment(payment);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a payment
     * DELETE /api/payments/{id}
     * 
     * @param id Payment ID to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a payment by ID
     * GET /api/payments/{id}
     * 
     * @param id Payment ID
     * @return Payment data with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer id) {
        return paymentService.findPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all payments
     * GET /api/payments
     * 
     * @return List of all payments with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payments by lease ID
     * GET /api/payments/lease/{leaseId}
     * 
     * @param leaseId Lease ID
     * @return List of payments for the specified lease
     */
    @GetMapping("/lease/{leaseId}")
    public ResponseEntity<List<Payment>> getPaymentsByLease(@PathVariable Integer leaseId) {
        List<Payment> payments = paymentService.findPaymentsByLease(leaseId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payments by status
     * GET /api/payments/status/{status}
     * 
     * @param status Payment status (PENDING, COMPLETED, FAILED, etc.)
     * @return List of payments with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<Payment> payments = paymentService.findPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get overdue payments
     * GET /api/payments/overdue
     * 
     * @return List of overdue payments
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<Payment>> getOverduePayments() {
        List<Payment> payments = paymentService.findOverduePayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payments within a date range
     * GET /api/payments/date-range?startDate=2024-01-01&endDate=2024-12-31
     * 
     * @param startDate Start date (format: yyyy-MM-dd)
     * @param endDate   End date (format: yyyy-MM-dd)
     * @return List of payments within the specified date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Payment> payments = paymentService.findPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    /**
     * Calculate total payments for a lease
     * GET /api/payments/lease/{leaseId}/total
     * 
     * @param leaseId Lease ID
     * @return Total amount of all payments for the lease
     */
    @GetMapping("/lease/{leaseId}/total")
    public ResponseEntity<BigDecimal> getTotalPaymentsForLease(@PathVariable Integer leaseId) {
        BigDecimal total = paymentService.calculateTotalPaymentsForLease(leaseId);
        return ResponseEntity.ok(total);
    }
}
