package com.rentflow.service;

import com.rentflow.model.*;
import com.rentflow.repository.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * ConsoleUIService - Business layer for console user interface
 * Encapsulates all console interaction logic, menu navigation, and user
 * prompts.
 * This separates presentation concerns from the main Spring Boot application.
 */
@Service
public class ConsoleUIService {

    private final PropertyRepository propertyRepo;
    private final TenantRepository tenantRepo;
    private final LeaseRepository leaseRepo;
    private final PaymentRepository paymentRepo;
    private final MaintenanceRequestRepository maintenanceRepo;
    private final ApplicationContext applicationContext;

    public ConsoleUIService(PropertyRepository propertyRepo, TenantRepository tenantRepo,
            LeaseRepository leaseRepo, PaymentRepository paymentRepo,
            MaintenanceRequestRepository maintenanceRepo, ApplicationContext applicationContext) {
        this.propertyRepo = propertyRepo;
        this.tenantRepo = tenantRepo;
        this.leaseRepo = leaseRepo;
        this.paymentRepo = paymentRepo;
        this.maintenanceRepo = maintenanceRepo;
        this.applicationContext = applicationContext;
    }

    /**
     * Entry point for console UI - main interaction loop
     */
    public void run(Scanner scanner) {
        boolean running = true;
        printWelcomeBanner();

        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            boolean shouldPause = true;

            switch (choice) {
                case "1":
                    showPaymentTracking();
                    break;
                case "2":
                    showAllProperties();
                    break;
                case "3":
                    showAllTenants();
                    break;
                case "4":
                    showAllLeases();
                    break;
                case "5":
                    showAllPayments();
                    break;
                case "6":
                    showPaymentSummary();
                    break;
                case "7":
                    showMaintenanceRequests();
                    break;
                case "8":
                    showOverduePayments();
                    break;
                case "9":
                    propertyManagementMenu(scanner);
                    shouldPause = false;
                    break;
                case "10":
                    tenantManagementMenu(scanner);
                    shouldPause = false;
                    break;
                case "11":
                    updateMaintenanceStatus(scanner);
                    shouldPause = false;
                    break;
                case "12":
                    System.out.println("\n❄️  Thank you for using RentFlow! Stay cold, stay focused! ❄️\n");
                    running = false;
                    break;
                default:
                    System.out.println("\n⚠️  Invalid option. Please try again.\n");
            }

            if (running && shouldPause) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
        // Shut down Spring Boot application
        System.exit(0);
    }

    private void printWelcomeBanner() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("❄️  RENTFLOW - PROFESSIONAL PROPERTY MANAGEMENT SYSTEM  ❄️");
        System.out.println("    Like ice, your data is crystal clear and solid!");
        System.out.println("=".repeat(70) + "\n");
    }

    private void printMainMenu() {
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
        System.out.println("9. 🏠 Property Management (CRUD)");
        System.out.println("10. 👥 Tenant Management (CRUD)");
        System.out.println("11. ✅ Update Maintenance Status");
        System.out.println("12. 🚪 Exit");
        System.out.println("=".repeat(70));
        System.out.print("Enter your choice: ");
    }

    @Transactional
    private void showPaymentTracking() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("💰 PAYMENT TRACKING - WHO OWES WHAT");
        System.out.println("=".repeat(70));

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

            if (paidAmount == null)
                paidAmount = BigDecimal.ZERO;
            if (outstanding == null)
                outstanding = BigDecimal.ZERO;

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
    private void showOverduePayments() {
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

    private void showPaymentSummary() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 PAYMENT SUMMARY REPORT");
        System.out.println("=".repeat(70));

        BigDecimal totalRentReceived = paymentRepo.getTotalRentReceived();
        BigDecimal totalRentExpected = paymentRepo.getTotalRentExpected(LocalDate.now());
        Long latePaymentCount = paymentRepo.countLatePayments();

        if (totalRentReceived == null)
            totalRentReceived = BigDecimal.ZERO;
        if (totalRentExpected == null)
            totalRentExpected = BigDecimal.ZERO;

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

    private void showAllProperties() {
        List<Property> properties = propertyRepo.findAll();
        printProperties("🏠 ALL PROPERTIES", properties);
    }

    private void showAllTenants() {
        List<Tenant> tenants = tenantRepo.findAll();
        printTenants("👥 ALL TENANTS", tenants);
    }

    @Transactional
    private void showAllLeases() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📄 ALL LEASES");
        System.out.println("=".repeat(70));

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
    private void showAllPayments() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("💵 ALL PAYMENTS");
        System.out.println("=".repeat(70));

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
    private void showMaintenanceRequests() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🔧 MAINTENANCE REQUESTS");
        System.out.println("=".repeat(70));

        List<MaintenanceRequest> requests = maintenanceRepo.findAllWithProperty();

        System.out.printf("\n%-5s %-25s %-12s %-12s %-15s\n",
                "ID", "PROPERTY", "PRIORITY", "STATUS", "EST. COST");
        System.out.println("-".repeat(69));

        for (MaintenanceRequest request : requests) {
            BigDecimal cost = request.getEstimatedCost() != null ? request.getEstimatedCost() : BigDecimal.ZERO;

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

    private void propertyExplorer(Scanner scanner) {
        boolean exploring = true;

        while (exploring) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("🧭 PROPERTY EXPLORER");
            System.out.println("=".repeat(70));
            System.out.println("1. List all properties");
            System.out.println("2. Filter by city");
            System.out.println("3. Filter by state");
            System.out.println("4. Filter by type");
            System.out.println("5. Filter by bedrooms");
            System.out.println("6. Filter by rent range");
            System.out.println("7. Back to main menu");
            System.out.println("=".repeat(70));
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    printProperties("🏠 ALL PROPERTIES", propertyRepo.findAll());
                    break;
                case "2": {
                    String city = prompt(scanner, "Enter city: ");
                    printProperties("🏙️  PROPERTIES IN " + city.toUpperCase(), propertyRepo.findByCity(city));
                    break;
                }
                case "3": {
                    String state = prompt(scanner, "Enter state (e.g., SC): ");
                    printProperties("🗺️  PROPERTIES IN " + state.toUpperCase(), propertyRepo.findByState(state));
                    break;
                }
                case "4": {
                    String typeInput = prompt(scanner, "Enter type (APARTMENT, HOUSE, CONDO, DUPLEX, SINGLE_FAMILY): ");
                    PropertyType type = parsePropertyType(typeInput);
                    if (type == null) {
                        System.out.println("\n⚠️  Invalid property type.\n");
                        break;
                    }
                    printProperties("🏷️  PROPERTIES - " + type, propertyRepo.findByPropertyType(type));
                    break;
                }
                case "5": {
                    String bedroomsInput = prompt(scanner, "Enter number of bedrooms: ");
                    Integer bedrooms = parseInteger(bedroomsInput);
                    if (bedrooms == null) {
                        System.out.println("\n⚠️  Invalid bedroom count.\n");
                        break;
                    }
                    printProperties("🛏️  " + bedrooms + " BEDROOM PROPERTIES", propertyRepo.findByBedrooms(bedrooms));
                    break;
                }
                case "6": {
                    String minInput = prompt(scanner, "Enter minimum rent: ");
                    String maxInput = prompt(scanner, "Enter maximum rent: ");
                    BigDecimal min = parseBigDecimal(minInput);
                    BigDecimal max = parseBigDecimal(maxInput);
                    if (min == null || max == null) {
                        System.out.println("\n⚠️  Invalid rent range.\n");
                        break;
                    }
                    printProperties("💰 RENT RANGE " + min + " - " + max,
                            propertyRepo.findByMonthlyRentBetween(min, max));
                    break;
                }
                case "7":
                    exploring = false;
                    break;
                default:
                    System.out.println("\n⚠️  Invalid option. Please try again.\n");
            }

            if (exploring) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private void propertyManagementMenu(Scanner scanner) {
        boolean managing = true;

        while (managing) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("🏠 PROPERTY MANAGEMENT (CRUD)");
            System.out.println("=".repeat(70));
            System.out.println("1. List properties");
            System.out.println("2. Property Explorer (filters)");
            System.out.println("3. Create property");
            System.out.println("4. Update property");
            System.out.println("5. Delete property");
            System.out.println("6. Back to main menu");
            System.out.println("=".repeat(70));
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    printProperties("🏠 ALL PROPERTIES", propertyRepo.findAll());
                    break;
                case "2":
                    propertyExplorer(scanner);
                    break;
                case "3":
                    createProperty(scanner);
                    break;
                case "4":
                    updateProperty(scanner);
                    break;
                case "5":
                    deleteProperty(scanner);
                    break;
                case "6":
                    managing = false;
                    break;
                default:
                    System.out.println("\n⚠️  Invalid option. Please try again.\n");
            }

            if (managing) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private void tenantManagementMenu(Scanner scanner) {
        boolean managing = true;

        while (managing) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("👥 TENANT MANAGEMENT (CRUD)");
            System.out.println("=".repeat(70));
            System.out.println("1. List tenants");
            System.out.println("2. Create tenant");
            System.out.println("3. Update tenant");
            System.out.println("4. Delete tenant");
            System.out.println("5. Back to main menu");
            System.out.println("=".repeat(70));
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    printTenants("👥 ALL TENANTS", tenantRepo.findAll());
                    break;
                case "2":
                    createTenant(scanner);
                    break;
                case "3":
                    updateTenant(scanner);
                    break;
                case "4":
                    deleteTenant(scanner);
                    break;
                case "5":
                    managing = false;
                    break;
                default:
                    System.out.println("\n⚠️  Invalid option. Please try again.\n");
            }

            if (managing) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private void createProperty(Scanner scanner) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("➕ CREATE PROPERTY");
        System.out.println("=".repeat(70));

        String address = prompt(scanner, "Address: ");
        String city = prompt(scanner, "City: ");
        String state = prompt(scanner, "State (e.g., SC): ");
        String zip = prompt(scanner, "Zip Code: ");
        String typeInput = prompt(scanner, "Type (APARTMENT, HOUSE, CONDO, DUPLEX, SINGLE_FAMILY): ");
        PropertyType type = parsePropertyType(typeInput);
        Integer bedrooms = parseInteger(prompt(scanner, "Bedrooms: "));
        BigDecimal bathrooms = parseBigDecimal(prompt(scanner, "Bathrooms (e.g., 1.5): "));
        BigDecimal rent = parseBigDecimal(prompt(scanner, "Monthly Rent: "));

        if (address.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty()
                || type == null || bedrooms == null || bathrooms == null || rent == null) {
            System.out.println("\n⚠️  Missing or invalid required fields.\n");
            return;
        }

        Property property = new Property();
        property.setAddress(address);
        property.setCity(city);
        property.setState(state);
        property.setZipCode(zip);
        property.setPropertyType(type);
        property.setBedrooms(bedrooms);
        property.setBathrooms(bathrooms);
        property.setMonthlyRent(rent);

        Integer squareFeet = parseInteger(prompt(scanner, "Square Feet (optional): "));
        if (squareFeet != null)
            property.setSquareFeet(squareFeet);

        String purchaseDateInput = prompt(scanner, "Purchase Date (YYYY-MM-DD, optional): ");
        if (!purchaseDateInput.isEmpty()) {
            LocalDate purchaseDate = parseDate(purchaseDateInput);
            if (purchaseDate != null)
                property.setPurchaseDate(purchaseDate);
        }

        String purchasePriceInput = prompt(scanner, "Purchase Price (optional): ");
        if (!purchasePriceInput.isEmpty()) {
            BigDecimal purchasePrice = parseBigDecimal(purchasePriceInput);
            if (purchasePrice != null)
                property.setPurchasePrice(purchasePrice);
        }

        propertyRepo.save(property);
        System.out.println("\n✅ Property created successfully.\n");
    }

    private void updateProperty(Scanner scanner) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✏️  UPDATE PROPERTY");
        System.out.println("=".repeat(70));

        Integer propertyId = parseInteger(prompt(scanner, "Enter property ID to update: "));
        if (propertyId == null) {
            System.out.println("\n⚠️  Invalid property ID.\n");
            return;
        }

        Property property = propertyRepo.findById(propertyId).orElse(null);
        if (property == null) {
            System.out.println("\n⚠️  Property not found.\n");
            return;
        }

        String address = prompt(scanner, "Address [" + property.getAddress() + "]: ");
        if (!address.isEmpty())
            property.setAddress(address);

        String city = prompt(scanner, "City [" + property.getCity() + "]: ");
        if (!city.isEmpty())
            property.setCity(city);

        String state = prompt(scanner, "State [" + property.getState() + "]: ");
        if (!state.isEmpty())
            property.setState(state);

        String zip = prompt(scanner, "Zip Code [" + property.getZipCode() + "]: ");
        if (!zip.isEmpty())
            property.setZipCode(zip);

        String typeInput = prompt(scanner, "Type [" + property.getPropertyType()
                + "] (APARTMENT, HOUSE, CONDO, DUPLEX, SINGLE_FAMILY): ");
        PropertyType type = parsePropertyType(typeInput);
        if (typeInput.isEmpty()) {
            // keep current
        } else if (type != null) {
            property.setPropertyType(type);
        } else {
            System.out.println("\n⚠️  Invalid property type. Keeping current value.\n");
        }

        String bedroomsInput = prompt(scanner, "Bedrooms [" + property.getBedrooms() + "]: ");
        if (!bedroomsInput.isEmpty()) {
            Integer bedrooms = parseInteger(bedroomsInput);
            if (bedrooms != null)
                property.setBedrooms(bedrooms);
        }

        String bathroomsInput = prompt(scanner, "Bathrooms [" + property.getBathrooms() + "]: ");
        if (!bathroomsInput.isEmpty()) {
            BigDecimal bathrooms = parseBigDecimal(bathroomsInput);
            if (bathrooms != null)
                property.setBathrooms(bathrooms);
        }

        String squareFeetInput = prompt(scanner, "Square Feet [" + nullSafe(property.getSquareFeet()) + "]: ");
        if (!squareFeetInput.isEmpty()) {
            Integer squareFeet = parseInteger(squareFeetInput);
            if (squareFeet != null)
                property.setSquareFeet(squareFeet);
        }

        String rentInput = prompt(scanner, "Monthly Rent [" + property.getMonthlyRent() + "]: ");
        if (!rentInput.isEmpty()) {
            BigDecimal rent = parseBigDecimal(rentInput);
            if (rent != null)
                property.setMonthlyRent(rent);
        }

        String purchaseDateInput = prompt(scanner,
                "Purchase Date [" + nullSafe(property.getPurchaseDate()) + "] (YYYY-MM-DD): ");
        if (!purchaseDateInput.isEmpty()) {
            LocalDate purchaseDate = parseDate(purchaseDateInput);
            if (purchaseDate != null)
                property.setPurchaseDate(purchaseDate);
        }

        String purchasePriceInput = prompt(scanner,
                "Purchase Price [" + nullSafe(property.getPurchasePrice()) + "]: ");
        if (!purchasePriceInput.isEmpty()) {
            BigDecimal purchasePrice = parseBigDecimal(purchasePriceInput);
            if (purchasePrice != null)
                property.setPurchasePrice(purchasePrice);
        }

        propertyRepo.save(property);
        System.out.println("\n✅ Property updated successfully.\n");
    }

    private void deleteProperty(Scanner scanner) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🗑️  DELETE PROPERTY");
        System.out.println("=".repeat(70));

        Integer propertyId = parseInteger(prompt(scanner, "Enter property ID to delete: "));
        if (propertyId == null) {
            System.out.println("\n⚠️  Invalid property ID.\n");
            return;
        }

        Property property = propertyRepo.findById(propertyId).orElse(null);
        if (property == null) {
            System.out.println("\n⚠️  Property not found.\n");
            return;
        }

        String confirm = prompt(scanner, "Type DELETE to confirm removal of " + property.getAddress() + ": ");
        if ("DELETE".equalsIgnoreCase(confirm)) {
            propertyRepo.delete(property);
            System.out.println("\n✅ Property deleted successfully.\n");
        } else {
            System.out.println("\nDeletion cancelled.\n");
        }
    }

    private void createTenant(Scanner scanner) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("➕ CREATE TENANT");
        System.out.println("=".repeat(70));

        String firstName = prompt(scanner, "First name: ");
        String lastName = prompt(scanner, "Last name: ");
        String email = prompt(scanner, "Email: ");
        String phone = prompt(scanner, "Phone: ");
        String statusInput = prompt(scanner, "Employment status (EMPLOYED, UNEMPLOYED, SELF_EMPLOYED, RETIRED): ");
        EmploymentStatus status = parseEmploymentStatus(statusInput);

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || status == null) {
            System.out.println("\n⚠️  Missing or invalid required fields.\n");
            return;
        }

        Tenant tenant = new Tenant();
        tenant.setFirstName(firstName);
        tenant.setLastName(lastName);
        tenant.setEmail(email);
        tenant.setPhone(phone);
        tenant.setEmploymentStatus(status);

        String incomeInput = prompt(scanner, "Monthly income (optional): ");
        if (!incomeInput.isEmpty()) {
            BigDecimal income = parseBigDecimal(incomeInput);
            if (income != null)
                tenant.setMonthlyIncome(income);
        }

        String dobInput = prompt(scanner, "Date of birth (YYYY-MM-DD, optional): ");
        if (!dobInput.isEmpty()) {
            LocalDate dob = parseDate(dobInput);
            if (dob != null)
                tenant.setDateOfBirth(dob);
        }

        String ssnLastFour = prompt(scanner, "SSN last four (optional): ");
        if (!ssnLastFour.isEmpty())
            tenant.setSsnLastFour(ssnLastFour);

        String emergencyName = prompt(scanner, "Emergency contact name (optional): ");
        if (!emergencyName.isEmpty())
            tenant.setEmergencyContactName(emergencyName);

        String emergencyPhone = prompt(scanner, "Emergency contact phone (optional): ");
        if (!emergencyPhone.isEmpty())
            tenant.setEmergencyContactPhone(emergencyPhone);

        tenantRepo.save(tenant);
        System.out.println("\n✅ Tenant created successfully.\n");
    }

    private void deleteTenant(Scanner scanner) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🗑️  DELETE TENANT");
        System.out.println("=".repeat(70));

        Integer tenantId = parseInteger(prompt(scanner, "Enter tenant ID to delete: "));
        if (tenantId == null) {
            System.out.println("\n⚠️  Invalid tenant ID.\n");
            return;
        }

        Tenant tenant = tenantRepo.findById(tenantId).orElse(null);
        if (tenant == null) {
            System.out.println("\n⚠️  Tenant not found.\n");
            return;
        }

        String confirm = prompt(scanner, "Type DELETE to confirm removal of " + tenant.getFullName() + ": ");
        if ("DELETE".equalsIgnoreCase(confirm)) {
            tenantRepo.delete(tenant);
            System.out.println("\n✅ Tenant deleted successfully.\n");
        } else {
            System.out.println("\nDeletion cancelled.\n");
        }
    }

    private void updateMaintenanceStatus(Scanner scanner) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ UPDATE MAINTENANCE STATUS");
        System.out.println("=".repeat(70));

        List<MaintenanceRequest> requests = maintenanceRepo.findAllWithProperty();
        if (requests.isEmpty()) {
            System.out.println("\nNo maintenance requests found.\n");
            return;
        }

        System.out.printf("\n%-5s %-25s %-12s %-12s\n",
                "ID", "PROPERTY", "PRIORITY", "STATUS");
        System.out.println("-".repeat(60));
        for (MaintenanceRequest request : requests) {
            System.out.printf("%-5d %-25s %-12s %-12s\n",
                    request.getRequestId(),
                    truncate(request.getProperty().getAddress(), 24),
                    request.getPriority(),
                    request.getStatus());
        }

        Integer requestId = parseInteger(prompt(scanner, "Enter maintenance request ID: "));
        if (requestId == null) {
            System.out.println("\n⚠️  Invalid request ID.\n");
            return;
        }

        MaintenanceRequest request = maintenanceRepo.findById(requestId).orElse(null);
        if (request == null) {
            System.out.println("\n⚠️  Maintenance request not found.\n");
            return;
        }

        System.out.println("Current status: " + request.getStatus());
        String statusInput = prompt(scanner, "New status (OPEN, IN_PROGRESS, COMPLETED, CANCELLED): ");
        MaintenanceStatus status = parseMaintenanceStatus(statusInput);
        if (status == null) {
            System.out.println("\n⚠️  Invalid status.\n");
            return;
        }

        request.setStatus(status);

        if (status == MaintenanceStatus.COMPLETED) {
            String completionInput = prompt(scanner, "Completion date (YYYY-MM-DD, optional): ");
            if (!completionInput.isEmpty()) {
                LocalDate completionDate = parseDate(completionInput);
                if (completionDate != null)
                    request.setCompletionDate(completionDate);
            } else {
                request.setCompletionDate(LocalDate.now());
            }
        }

        String actualCostInput = prompt(scanner, "Actual cost (optional): ");
        if (!actualCostInput.isEmpty()) {
            BigDecimal actualCost = parseBigDecimal(actualCostInput);
            if (actualCost != null)
                request.setActualCost(actualCost);
        }

        String notesInput = prompt(scanner, "Notes (optional): ");
        if (!notesInput.isEmpty())
            request.setNotes(notesInput);

        maintenanceRepo.save(request);
        System.out.println("\n✅ Maintenance request updated successfully.\n");
    }

    private void printTenants(String title, List<Tenant> tenants) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(title);
        System.out.println("=".repeat(70));

        if (tenants.isEmpty()) {
            System.out.println("\nNo tenants found.\n");
            return;
        }

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

    private void updateTenant(Scanner scanner) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✏️  UPDATE TENANT");
        System.out.println("=".repeat(70));

        String idInput = prompt(scanner, "Enter tenant ID to update: ");
        Integer tenantId = parseInteger(idInput);
        if (tenantId == null) {
            System.out.println("\n⚠️  Invalid tenant ID.\n");
            return;
        }

        Tenant tenant = tenantRepo.findById(tenantId).orElse(null);
        if (tenant == null) {
            System.out.println("\n⚠️  Tenant not found.\n");
            return;
        }

        System.out.println("Current tenant: " + tenant.getFullName() + " (" + tenant.getEmail() + ")");

        String firstName = prompt(scanner, "First name [" + tenant.getFirstName() + "]: ");
        if (!firstName.isEmpty())
            tenant.setFirstName(firstName);

        String lastName = prompt(scanner, "Last name [" + tenant.getLastName() + "]: ");
        if (!lastName.isEmpty())
            tenant.setLastName(lastName);

        String email = prompt(scanner, "Email [" + tenant.getEmail() + "]: ");
        if (!email.isEmpty())
            tenant.setEmail(email);

        String phone = prompt(scanner, "Phone [" + tenant.getPhone() + "]: ");
        if (!phone.isEmpty())
            tenant.setPhone(phone);

        String statusInput = prompt(scanner,
                "Employment status [" + tenant.getEmploymentStatus()
                        + "] (EMPLOYED, UNEMPLOYED, SELF_EMPLOYED, RETIRED): ");
        EmploymentStatus status = parseEmploymentStatus(statusInput);
        if (statusInput.isEmpty()) {
            // keep current
        } else if (status != null) {
            tenant.setEmploymentStatus(status);
        } else {
            System.out.println("\n⚠️  Invalid employment status. Keeping current value.\n");
        }

        String incomeInput = prompt(scanner, "Monthly income [" + tenant.getMonthlyIncome() + "]: ");
        if (!incomeInput.isEmpty()) {
            BigDecimal income = parseBigDecimal(incomeInput);
            if (income != null) {
                tenant.setMonthlyIncome(income);
            } else {
                System.out.println("\n⚠️  Invalid income. Keeping current value.\n");
            }
        }

        String emergencyName = prompt(scanner,
                "Emergency contact name [" + nullSafe(tenant.getEmergencyContactName()) + "]: ");
        if (!emergencyName.isEmpty())
            tenant.setEmergencyContactName(emergencyName);

        String emergencyPhone = prompt(scanner,
                "Emergency contact phone [" + nullSafe(tenant.getEmergencyContactPhone()) + "]: ");
        if (!emergencyPhone.isEmpty())
            tenant.setEmergencyContactPhone(emergencyPhone);

        String dobInput = prompt(scanner,
                "Date of birth [" + nullSafe(tenant.getDateOfBirth()) + "] (YYYY-MM-DD): ");
        if (!dobInput.isEmpty()) {
            LocalDate dob = parseDate(dobInput);
            if (dob != null) {
                tenant.setDateOfBirth(dob);
            } else {
                System.out.println("\n⚠️  Invalid date. Keeping current value.\n");
            }
        }

        tenantRepo.save(tenant);
        System.out.println("\n✅ Tenant updated successfully.\n");
    }

    private void printProperties(String title, List<Property> properties) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(title);
        System.out.println("=".repeat(70));

        if (properties.isEmpty()) {
            System.out.println("\nNo properties found.\n");
            return;
        }

        System.out.printf("\n%-5s %-30s %-15s %-15s %-10s\n",
                "ID", "ADDRESS", "CITY", "TYPE", "RENT");
        System.out.println("-".repeat(78));

        for (Property property : properties) {
            System.out.printf("%-5d %-30s %-15s %-15s $%-9.2f\n",
                    property.getPropertyId(),
                    truncate(property.getAddress(), 29),
                    truncate(property.getCity(), 14),
                    property.getPropertyType(),
                    property.getMonthlyRent());
        }
        System.out.println("\nTotal Properties: " + properties.size());
        System.out.println("=".repeat(70));
    }

    // ==================== Helper Methods ====================

    private static String prompt(Scanner scanner, String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    private static Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static PropertyType parsePropertyType(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            String normalized = value.trim().toUpperCase();
            if ("HOUSE".equals(normalized)) {
                return PropertyType.SINGLE_FAMILY;
            }
            return PropertyType.valueOf(normalized);
        } catch (Exception e) {
            return null;
        }
    }

    private static MaintenanceStatus parseMaintenanceStatus(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return MaintenanceStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private static EmploymentStatus parseEmploymentStatus(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return EmploymentStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private static String nullSafe(Object value) {
        return value == null ? "" : value.toString();
    }

    private static String truncate(String str, int maxLength) {
        if (str == null)
            return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
}
