-- RentFlow Property Manager - Database Schema
-- Like the cold, this structure is SOLID and UNBREAKABLE!

DROP DATABASE IF EXISTS rentflow_pm;
CREATE DATABASE rentflow_pm;
USE rentflow_pm;

-- Table 1: Properties (The Foundation)
CREATE TABLE properties (
    property_id INT PRIMARY KEY AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    property_type ENUM('SINGLE_FAMILY', 'DUPLEX', 'APARTMENT', 'CONDO') NOT NULL,
    bedrooms INT NOT NULL,
    bathrooms DECIMAL(3,1) NOT NULL,
    square_feet INT,
    monthly_rent DECIMAL(10,2) NOT NULL,
    purchase_date DATE,
    purchase_price DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_bedrooms CHECK (bedrooms > 0),
    CONSTRAINT chk_bathrooms CHECK (bathrooms > 0),
    CONSTRAINT chk_rent CHECK (monthly_rent > 0)
);

-- Table 2: Tenants (The People)
CREATE TABLE tenants (
    tenant_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    date_of_birth DATE,
    ssn_last_four VARCHAR(4),
    employment_status ENUM('EMPLOYED', 'SELF_EMPLOYED', 'UNEMPLOYED', 'RETIRED') NOT NULL,
    monthly_income DECIMAL(10,2),
    emergency_contact_name VARCHAR(200),
    emergency_contact_phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_ssn CHECK (ssn_last_four REGEXP '^[0-9]{4}$')
);

-- Table 3: Leases (The Contracts - Connects Property and Tenant)
CREATE TABLE leases (
    lease_id INT PRIMARY KEY AUTO_INCREMENT,
    property_id INT NOT NULL,
    tenant_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    monthly_rent DECIMAL(10,2) NOT NULL,
    security_deposit DECIMAL(10,2) NOT NULL,
    lease_status ENUM('ACTIVE', 'EXPIRED', 'TERMINATED', 'PENDING') NOT NULL DEFAULT 'PENDING',
    lease_terms TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(property_id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id) ON DELETE CASCADE,
    CONSTRAINT chk_dates CHECK (end_date > start_date),
    CONSTRAINT chk_lease_rent CHECK (monthly_rent > 0),
    CONSTRAINT chk_deposit CHECK (security_deposit >= 0)
);

-- Table 4: Payments (THE HEART - The Money Flow!)
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    lease_id INT NOT NULL,
    payment_date DATE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_type ENUM('RENT', 'SECURITY_DEPOSIT', 'LATE_FEE', 'UTILITY', 'OTHER') NOT NULL DEFAULT 'RENT',
    payment_method ENUM('CASH', 'CHECK', 'BANK_TRANSFER', 'CREDIT_CARD', 'VENMO', 'ZELLE') NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    due_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lease_id) REFERENCES leases(lease_id) ON DELETE CASCADE,
    CONSTRAINT chk_amount CHECK (amount > 0)
);

-- Table 5: Maintenance Requests (The Repairs)
CREATE TABLE maintenance_requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    property_id INT NOT NULL,
    tenant_id INT,
    request_date DATE NOT NULL,
    description TEXT NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') NOT NULL DEFAULT 'MEDIUM',
    status ENUM('OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'OPEN',
    estimated_cost DECIMAL(10,2),
    actual_cost DECIMAL(10,2),
    completion_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(property_id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id) ON DELETE SET NULL,
    CONSTRAINT chk_costs CHECK (estimated_cost IS NULL OR estimated_cost >= 0),
    CONSTRAINT chk_actual CHECK (actual_cost IS NULL OR actual_cost >= 0)
);

-- Create indexes for better query performance (Like breathing efficiently!)
CREATE INDEX idx_property_city ON properties(city);
CREATE INDEX idx_tenant_email ON tenants(email);
CREATE INDEX idx_lease_status ON leases(lease_status);
CREATE INDEX idx_lease_dates ON leases(start_date, end_date);
CREATE INDEX idx_payment_status ON payments(payment_status);
CREATE INDEX idx_payment_date ON payments(payment_date);
CREATE INDEX idx_maintenance_status ON maintenance_requests(status);
CREATE INDEX idx_maintenance_priority ON maintenance_requests(priority);
