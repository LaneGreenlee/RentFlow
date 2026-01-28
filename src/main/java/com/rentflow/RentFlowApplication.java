package com.rentflow;

import com.rentflow.model.*;
import com.rentflow.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class RentFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentFlowApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(PropertyRepository propertyRepo,
                                TenantRepository tenantRepo,
                                LeaseRepository leaseRepo,
                                PaymentRepository paymentRepo,
                                MaintenanceRequestRepository maintenanceRepo) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            printWelcomeBanner();

            while (running) {
                printMainMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        showPaymentTracking(paymentRepo, leaseRepo);
                        break;
                    case "2":
                        showAllProperties(propertyRepo);
                        break;
                    case "3":
                        showAllTenants(tenantRepo);
                        break;
                    case "4":
                        showAllLeases(leaseRepo);
                        break;
                    case "5":
                        showAllPayments(paymentRepo);
                        break;
                    case "6":
                        showPaymentSummary(paymentRepo, leaseRepo);
                        break;
                    case "7":
                        showMaintenanceRequests(maintenanceRepo);
                        break;
                    case "8":
                        showOverduePayments(paymentRepo);
                        break;
                    case "9":
                        System.out.println("\n❄️  Thank you for using RentFlow! Stay cold, stay focused! ❄️\n");
                        running = false;
                        break;
                    default:
                        System.out.println("\n⚠️  Invalid option. Please try again.\n");
                }

                if (running) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }

            scanner.close();
        };
    }

    private static void printWelcomeBanner() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("❄️  RENTFLOW - PROFESSIONAL PROPERTY MANAGEMENT SYSTEM  ❄️");
        System.out.println("    Like ice, your data is crystal clear and solid!");
        System.out.println("=".repeat(70) + "\n");
    }

    private static void printMainMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(70));
        System.out.println("1. 💰 PAYMENT TRACKING - Who Owes What");
        System.out.println("2. 🏠 View All Properties");
        System.out.println("3. 👥 View All Tenants");
        System.out.println("4. 📄 View All Leases");
        System.out.println("5. 💵 View All Payments");
        System.out.println("6. 📊 Payment Summary Report");
        System.out.println("7. 🔧 View Maintenance Requests");
        System.out.println("8. ⚠️  View Overdue Payments");
        System.out.println("9. 🚪 Exit");
        System.out.println("=".repeat(70));
        System.out.print("Enter your choice: ");
    }

    @Transactional
    private void showPaymentTracking(PaymentRepository paymentRepo, LeaseRepository leaseRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("💰 PAYMENT TRACKING - WHO OWES WHAT");
        System.out.println("=".repeat(70));

        // Use fetch-join query to initialize property and tenant while the session is open
        List<Lease> activeLeases = leaseRepo.findAllActiveLeasesWithPropertyAndTenant();
        LocalDate today = LocalDate.now();

        System.out.printf("\n%-25s %-20s %-15s %-15s %-15s\n",
                "PROPERTY", "TENANT", "MONTHLY RENT", "PAID", "OUTSTANDING");
        System.out.println("-".repeat(90));

        BigDecimal totalRent = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalOutstanding = BigDecimal.ZERO;

        for (Lease lease : activeLeases) {
            String propertyAddr = lease.getProperty().getAddress();
            String tenantName = lease.getTenant().getFullName();
            BigDecimal monthlyRent = lease.getMonthlyRent();

            BigDecimal paidAmount = paymentRepo.getTotalPaymentsForLease(lease.getLeaseId());
            BigDecimal outstanding = paymentRepo.getOutstandingBalanceForLease(lease.getLeaseId(), today);

            if (paidAmount == null) paidAmount = BigDecimal.ZERO;
            if (outstanding == null) outstanding = BigDecimal.ZERO;

            totalRent = totalRent.add(monthlyRent);
            totalPaid = totalPaid.add(paidAmount);
            totalOutstanding = totalOutstanding.add(outstanding);

            String status = outstanding.compareTo(BigDecimal.ZERO) > 0 ? "❌ OWES" : "✅ PAID";

            System.out.printf("%-25s %-20s $%-14.2f $%-14.2f $%-14.2f %s\n",
                    truncate(propertyAddr, 24),
                    truncate(tenantName, 19),
                    monthlyRent,
                    paidAmount,
                    outstanding,
                    status);
        }

        System.out.println("-".repeat(90));
        System.out.printf("%-25s %-20s $%-14.2f $%-14.2f $%-14.2f\n",
                "TOTALS:", "",
                totalRent,
                totalPaid,
                totalOutstanding);
        System.out.println("=".repeat(70));
    }

    @Transactional
    private void showOverduePayments(PaymentRepository paymentRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("⚠️  OVERDUE PAYMENTS");
        System.out.println("=".repeat(70));

        List<Payment> overduePayments = paymentRepo.findOverduePayments(LocalDate.now());

        if (overduePayments.isEmpty()) {
            System.out.println("\n✅ No overdue payments! Everyone is current!\n");
        } else {
            System.out.printf("\n%-15s %-25s %-15s %-15s %-15s\n",
                    "DUE DATE", "TENANT", "PROPERTY", "AMOUNT", "DAYS OVERDUE");
            System.out.println("-".repeat(85));

            for (Payment payment : overduePayments) {
                String tenantName = payment.getLease().getTenant().getFullName();
                String property = payment.getLease().getProperty().getAddress();
                long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(
                        payment.getDueDate(), LocalDate.now());

                System.out.printf("%-15s %-25s %-15s $%-14.2f %d days\n",
                        payment.getDueDate(),
                        truncate(tenantName, 24),
                        truncate(property, 14),
                        payment.getAmount(),
                        daysOverdue);
            }
            System.out.println("=".repeat(70));
        }
    }

    private void showPaymentSummary(PaymentRepository paymentRepo, LeaseRepository leaseRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 PAYMENT SUMMARY REPORT");
        System.out.println("=".repeat(70));

        BigDecimal totalRentReceived = paymentRepo.getTotalRentReceived();
        BigDecimal totalRentExpected = paymentRepo.getTotalRentExpected(LocalDate.now());
        Long latePaymentCount = paymentRepo.countLatePayments();

        if (totalRentReceived == null) totalRentReceived = BigDecimal.ZERO;
        if (totalRentExpected == null) totalRentExpected = BigDecimal.ZERO;

        BigDecimal collectionRate = BigDecimal.ZERO;
        if (totalRentExpected.compareTo(BigDecimal.ZERO) > 0) {
            collectionRate = totalRentReceived.divide(totalRentExpected, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        System.out.printf("\nTotal Rent Received:     $%,.2f\n", totalRentReceived);
        System.out.printf("Total Rent Expected:     $%,.2f\n", totalRentExpected);
        System.out.printf("Collection Rate:         %.2f%%\n", collectionRate);
        System.out.printf("Late Payment Count:      %d\n", latePaymentCount);
        System.out.println("\n" + "=".repeat(70));
    }

    private void showAllProperties(PropertyRepository propertyRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🏠 ALL PROPERTIES");
        System.out.println("=".repeat(70));

        List<Property> properties = propertyRepo.findAll();

        System.out.printf("\n%-5s %-30s %-15s %-10s %-10s\n",
                "ID", "ADDRESS", "CITY", "TYPE", "RENT");
        System.out.println("-".repeat(70));

        for (Property property : properties) {
            System.out.printf("%-5d %-30s %-15s %-10s $%-9.2f\n",
                    property.getPropertyId(),
                    truncate(property.getAddress(), 29),
                    truncate(property.getCity(), 14),
                    property.getPropertyType(),
                    property.getMonthlyRent());
        }
        System.out.println("\nTotal Properties: " + properties.size());
        System.out.println("=".repeat(70));
    }

    private void showAllTenants(TenantRepository tenantRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("👥 ALL TENANTS");
        System.out.println("=".repeat(70));

        List<Tenant> tenants = tenantRepo.findAll();

        System.out.printf("\n%-5s %-25s %-30s %-15s\n",
                "ID", "NAME", "EMAIL", "STATUS");
        System.out.println("-".repeat(75));

        for (Tenant tenant : tenants) {
            System.out.printf("%-5d %-25s %-30s %-15s\n",
                    tenant.getTenantId(),
                    truncate(tenant.getFullName(), 24),
                    truncate(tenant.getEmail(), 29),
                    tenant.getEmploymentStatus());
        }
        System.out.println("\nTotal Tenants: " + tenants.size());
        System.out.println("=".repeat(70));
    }

    @Transactional
    private void showAllLeases(LeaseRepository leaseRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📄 ALL LEASES");
        System.out.println("=".repeat(70));

        // fetch property and tenant with join to avoid lazy init
        List<Lease> leases = leaseRepo.findAllWithPropertyAndTenant();

        System.out.printf("\n%-5s %-25s %-20s %-12s %-10s\n",
                "ID", "PROPERTY", "TENANT", "STATUS", "RENT");
        System.out.println("-".repeat(72));

        for (Lease lease : leases) {
            System.out.printf("%-5d %-25s %-20s %-12s $%-9.2f\n",
                    lease.getLeaseId(),
                    truncate(lease.getProperty().getAddress(), 24),
                    truncate(lease.getTenant().getFullName(), 19),
                    lease.getLeaseStatus(),
                    lease.getMonthlyRent());
        }
        System.out.println("\nTotal Leases: " + leases.size());
        System.out.println("=".repeat(70));
    }

    @Transactional
    private void showAllPayments(PaymentRepository paymentRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("💵 ALL PAYMENTS");
        System.out.println("=".repeat(70));

        // Use fetch-join to initialize associated lease and tenant to avoid lazy-init issues
        List<Payment> payments = paymentRepo.findAllWithLeaseAndTenant();

        System.out.printf("\n%-5s %-25s %-12s %-10s %-12s\n",
                "ID", "TENANT", "DATE", "AMOUNT", "STATUS");
        System.out.println("-".repeat(64));

        for (Payment payment : payments) {
            System.out.printf("%-5d %-25s %-12s $%-9.2f %-12s\n",
                    payment.getPaymentId(),
                    truncate(payment.getLease().getTenant().getFullName(), 24),
                    payment.getPaymentDate(),
                    payment.getAmount(),
                    payment.getPaymentStatus());
        }
        System.out.println("\nTotal Payments: " + payments.size());
        System.out.println("=".repeat(70));
    }

    @Transactional
    private void showMaintenanceRequests(MaintenanceRequestRepository maintenanceRepo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🔧 MAINTENANCE REQUESTS");
        System.out.println("=".repeat(70));

        // Use fetch-join to initialize property while the session is not open elsewhere
        List<MaintenanceRequest> requests = maintenanceRepo.findAllWithProperty();

        System.out.printf("\n%-5s %-25s %-12s %-12s %-15s\n",
                "ID", "PROPERTY", "PRIORITY", "STATUS", "EST. COST");
        System.out.println("-".repeat(69));

        for (MaintenanceRequest request : requests) {
            BigDecimal cost = request.getEstimatedCost() != null ?
                    request.getEstimatedCost() : BigDecimal.ZERO;

            System.out.printf("%-5d %-25s %-12s %-12s $%-14.2f\n",
                    request.getRequestId(),
                    truncate(request.getProperty().getAddress(), 24),
                    request.getPriority(),
                    request.getStatus(),
                    cost);
        }
        System.out.println("\nTotal Maintenance Requests: " + requests.size());
        System.out.println("=".repeat(70));
    }

    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
}
