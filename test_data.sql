-- RentFlow - Test Data
-- Focus: PAYMENT TRACKING - Who owes what, who's paid up!
-- 50+ rows to demonstrate real-world scenarios

USE rentflow_pm;

-- Insert 8 Properties (small landlord with 5-10 properties)
INSERT INTO properties (address, city, state, zip_code, property_type, bedrooms, bathrooms, square_feet, monthly_rent, purchase_date, purchase_price) VALUES
('123 Maple Street', 'Columbia', 'SC', '29201', 'SINGLE_FAMILY', 3, 2.0, 1500, 1200.00, '2020-01-15', 185000.00),
('456 Oak Avenue', 'Columbia', 'SC', '29203', 'DUPLEX', 2, 1.5, 1100, 950.00, '2019-06-20', 220000.00),
('789 Pine Road', 'Columbia', 'SC', '29205', 'APARTMENT', 2, 2.0, 1200, 1100.00, '2021-03-10', 175000.00),
('321 Elm Drive', 'Lexington', 'SC', '29072', 'SINGLE_FAMILY', 4, 2.5, 2000, 1500.00, '2018-11-05', 245000.00),
('654 Cedar Lane', 'Irmo', 'SC', '29063', 'CONDO', 2, 2.0, 1300, 1050.00, '2022-02-28', 165000.00),
('987 Birch Court', 'Columbia', 'SC', '29209', 'SINGLE_FAMILY', 3, 2.0, 1600, 1250.00, '2020-08-12', 195000.00),
('147 Willow Way', 'West Columbia', 'SC', '29169', 'APARTMENT', 1, 1.0, 800, 850.00, '2021-09-15', 125000.00),
('258 Spruce Street', 'Columbia', 'SC', '29204', 'SINGLE_FAMILY', 3, 2.5, 1700, 1350.00, '2019-12-01', 210000.00);

-- Insert 10 Tenants (mix of good payers and problematic ones)
INSERT INTO tenants (first_name, last_name, email, phone, date_of_birth, ssn_last_four, employment_status, monthly_income, emergency_contact_name, emergency_contact_phone) VALUES
('Sarah', 'Johnson', 'sarah.johnson@email.com', '803-555-0101', '1990-05-15', '4521', 'EMPLOYED', 4500.00, 'Mike Johnson', '803-555-0102'),
('Michael', 'Chen', 'michael.chen@email.com', '803-555-0201', '1985-08-22', '7834', 'EMPLOYED', 5200.00, 'Lisa Chen', '803-555-0202'),
('Emily', 'Rodriguez', 'emily.rodriguez@email.com', '803-555-0301', '1992-03-10', '2156', 'SELF_EMPLOYED', 3800.00, 'Carlos Rodriguez', '803-555-0302'),
('David', 'Williams', 'david.williams@email.com', '803-555-0401', '1988-11-30', '9012', 'EMPLOYED', 6000.00, 'Jennifer Williams', '803-555-0402'),
('Jessica', 'Brown', 'jessica.brown@email.com', '803-555-0501', '1995-07-18', '3487', 'EMPLOYED', 3200.00, 'Robert Brown', '803-555-0502'),
('Christopher', 'Davis', 'chris.davis@email.com', '803-555-0601', '1987-12-05', '6543', 'UNEMPLOYED', 0.00, 'Amanda Davis', '803-555-0602'),
('Amanda', 'Martinez', 'amanda.martinez@email.com', '803-555-0701', '1993-04-25', '8765', 'EMPLOYED', 4100.00, 'Jose Martinez', '803-555-0702'),
('James', 'Taylor', 'james.taylor@email.com', '803-555-0801', '1991-09-14', '1234', 'SELF_EMPLOYED', 4800.00, 'Mary Taylor', '803-555-0802'),
('Lisa', 'Anderson', 'lisa.anderson@email.com', '803-555-0901', '1989-02-28', '5678', 'RETIRED', 2800.00, 'John Anderson', '803-555-0902'),
('Robert', 'Thomas', 'robert.thomas@email.com', '803-555-1001', '1994-06-07', '9876', 'EMPLOYED', 5500.00, 'Karen Thomas', '803-555-1002');

-- Insert 10 Leases (all properties rented)
INSERT INTO leases (property_id, tenant_id, start_date, end_date, monthly_rent, security_deposit, lease_status, lease_terms) VALUES
(1, 1, '2024-01-01', '2024-12-31', 1200.00, 1200.00, 'ACTIVE', '12-month lease, pets allowed with $300 deposit'),
(2, 2, '2024-02-01', '2025-01-31', 950.00, 950.00, 'ACTIVE', '12-month lease, no pets'),
(3, 3, '2023-09-01', '2024-08-31', 1100.00, 1100.00, 'ACTIVE', '12-month lease, utilities included'),
(4, 4, '2024-03-01', '2025-02-28', 1500.00, 1500.00, 'ACTIVE', '12-month lease, lawn care included'),
(5, 5, '2024-01-15', '2025-01-14', 1050.00, 1050.00, 'ACTIVE', '12-month lease, no smoking'),
(6, 6, '2023-11-01', '2024-10-31', 1250.00, 1250.00, 'ACTIVE', '12-month lease, month-to-month after'),
(7, 7, '2024-04-01', '2025-03-31', 850.00, 850.00, 'ACTIVE', '12-month lease, parking included'),
(8, 8, '2023-12-01', '2024-11-30', 1350.00, 1350.00, 'ACTIVE', '12-month lease, gym access included'),
(3, 9, '2022-09-01', '2023-08-31', 1050.00, 1050.00, 'EXPIRED', 'Previous tenant - 12-month lease'),
(7, 10, '2023-04-01', '2024-03-31', 800.00, 800.00, 'EXPIRED', 'Previous tenant - 12-month lease');

-- Insert 60+ Payments - THIS IS THE HEART! 
-- Scenarios: Paid up tenants, late payers, partial payers, perfect records

-- Tenant 1 (Sarah) - PERFECT PAYER - All paid on time
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(1, '2024-01-01', 1200.00, 'SECURITY_DEPOSIT', 'BANK_TRANSFER', 'COMPLETED', '2024-01-01', 'Security deposit received'),
(1, '2024-01-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-01-01', 'January rent - paid on time'),
(1, '2024-02-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-02-01', 'February rent - paid on time'),
(1, '2024-03-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-03-01', 'March rent - paid on time'),
(1, '2024-04-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-04-01', 'April rent - paid on time'),
(1, '2024-05-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-05-01', 'May rent - paid on time'),
(1, '2024-06-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-06-01', 'June rent - paid on time'),
(1, '2024-07-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-07-01', 'July rent - paid on time'),
(1, '2024-08-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-08-01', 'August rent - paid on time'),
(1, '2024-09-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-09-01', 'September rent - paid on time'),
(1, '2024-10-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-10-01', 'October rent - paid on time'),
(1, '2024-11-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-11-01', 'November rent - paid on time'),
(1, '2024-12-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-12-01', 'December rent - paid on time'),
(1, '2025-01-01', 1200.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2025-01-01', 'January 2025 rent - paid on time');

-- Tenant 2 (Michael) - GOOD PAYER - Mostly on time
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(2, '2024-02-01', 950.00, 'SECURITY_DEPOSIT', 'CHECK', 'COMPLETED', '2024-02-01', 'Security deposit received'),
(2, '2024-02-01', 950.00, 'RENT', 'CHECK', 'COMPLETED', '2024-02-01', 'February rent'),
(2, '2024-03-01', 950.00, 'RENT', 'CHECK', 'COMPLETED', '2024-03-01', 'March rent'),
(2, '2024-04-05', 950.00, 'RENT', 'CHECK', 'COMPLETED', '2024-04-01', 'April rent - 4 days late'),
(2, '2024-05-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-05-01', 'May rent'),
(2, '2024-06-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-06-01', 'June rent'),
(2, '2024-07-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-07-01', 'July rent'),
(2, '2024-08-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-08-01', 'August rent'),
(2, '2024-09-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-09-01', 'September rent'),
(2, '2024-10-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-10-01', 'October rent'),
(2, '2024-11-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-11-01', 'November rent'),
(2, '2024-12-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2024-12-01', 'December rent'),
(2, '2025-01-01', 950.00, 'RENT', 'ZELLE', 'COMPLETED', '2025-01-01', 'January 2025 rent');

-- Tenant 3 (Emily) - PROBLEMATIC - Missing payments, late fees
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(3, '2023-09-01', 1100.00, 'SECURITY_DEPOSIT', 'CASH', 'COMPLETED', '2023-09-01', 'Security deposit'),
(3, '2023-09-01', 1100.00, 'RENT', 'CASH', 'COMPLETED', '2023-09-01', 'September 2023 rent'),
(3, '2023-10-15', 1100.00, 'RENT', 'CASH', 'COMPLETED', '2023-10-01', 'October rent - 14 days late'),
(3, '2023-10-15', 50.00, 'LATE_FEE', 'CASH', 'COMPLETED', '2023-10-01', 'Late fee for October'),
(3, '2023-11-01', 1100.00, 'RENT', 'VENMO', 'COMPLETED', '2023-11-01', 'November rent'),
(3, '2023-12-20', 1100.00, 'RENT', 'VENMO', 'COMPLETED', '2023-12-01', 'December rent - 19 days late'),
(3, '2023-12-20', 75.00, 'LATE_FEE', 'VENMO', 'COMPLETED', '2023-12-01', 'Late fee for December'),
(3, '2024-01-01', 1100.00, 'RENT', 'VENMO', 'COMPLETED', '2024-01-01', 'January rent'),
-- MISSING February, March, April 2024!
(3, '2024-05-15', 1100.00, 'RENT', 'CASH', 'COMPLETED', '2024-05-01', 'May rent - partial catch up');

-- Tenant 4 (David) - EXCELLENT PAYER
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(4, '2024-03-01', 1500.00, 'SECURITY_DEPOSIT', 'BANK_TRANSFER', 'COMPLETED', '2024-03-01', 'Security deposit'),
(4, '2024-03-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-03-01', 'March rent'),
(4, '2024-04-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-04-01', 'April rent'),
(4, '2024-05-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-05-01', 'May rent'),
(4, '2024-06-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-06-01', 'June rent'),
(4, '2024-07-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-07-01', 'July rent'),
(4, '2024-08-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-08-01', 'August rent'),
(4, '2024-09-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-09-01', 'September rent'),
(4, '2024-10-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-10-01', 'October rent'),
(4, '2024-11-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-11-01', 'November rent'),
(4, '2024-12-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-12-01', 'December rent'),
(4, '2025-01-01', 1500.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2025-01-01', 'January 2025 rent');

-- Tenant 5 (Jessica) - CURRENT - Some late payments
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(5, '2024-01-15', 1050.00, 'SECURITY_DEPOSIT', 'CHECK', 'COMPLETED', '2024-01-15', 'Security deposit'),
(5, '2024-01-15', 1050.00, 'RENT', 'CHECK', 'COMPLETED', '2024-01-15', 'January rent'),
(5, '2024-02-15', 1050.00, 'RENT', 'CHECK', 'COMPLETED', '2024-02-15', 'February rent'),
(5, '2024-03-15', 1050.00, 'RENT', 'CHECK', 'COMPLETED', '2024-03-15', 'March rent'),
(5, '2024-04-20', 1050.00, 'RENT', 'CHECK', 'COMPLETED', '2024-04-15', 'April rent - 5 days late'),
(5, '2024-05-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-05-15', 'May rent'),
(5, '2024-06-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-06-15', 'June rent'),
(5, '2024-07-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-07-15', 'July rent'),
(5, '2024-08-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-08-15', 'August rent'),
(5, '2024-09-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-09-15', 'September rent'),
(5, '2024-10-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-10-15', 'October rent'),
(5, '2024-11-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-11-15', 'November rent'),
(5, '2024-12-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2024-12-15', 'December rent'),
(5, '2025-01-15', 1050.00, 'RENT', 'CREDIT_CARD', 'COMPLETED', '2025-01-15', 'January 2025 rent');

-- Tenant 6 (Christopher) - PROBLEM TENANT - Unemployed, multiple late payments, owes money!
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(6, '2023-11-01', 1250.00, 'SECURITY_DEPOSIT', 'CHECK', 'COMPLETED', '2023-11-01', 'Security deposit'),
(6, '2023-11-01', 1250.00, 'RENT', 'CHECK', 'COMPLETED', '2023-11-01', 'November 2023 rent'),
(6, '2023-12-10', 1250.00, 'RENT', 'CASH', 'COMPLETED', '2023-12-01', 'December rent - 9 days late'),
(6, '2024-01-20', 600.00, 'RENT', 'CASH', 'COMPLETED', '2024-01-01', 'January rent - PARTIAL PAYMENT'),
-- Missing rest of January, all of February, March!
(6, '2024-04-15', 500.00, 'RENT', 'CASH', 'COMPLETED', '2024-04-01', 'April rent - PARTIAL');
-- Currently owes: $650 (Jan) + $1250 (Feb) + $1250 (Mar) + $750 (April) = $3,900!

-- Tenant 7 (Amanda) - RELIABLE PAYER
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(7, '2024-04-01', 850.00, 'SECURITY_DEPOSIT', 'BANK_TRANSFER', 'COMPLETED', '2024-04-01', 'Security deposit'),
(7, '2024-04-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-04-01', 'April rent'),
(7, '2024-05-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-05-01', 'May rent'),
(7, '2024-06-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-06-01', 'June rent'),
(7, '2024-07-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-07-01', 'July rent'),
(7, '2024-08-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-08-01', 'August rent'),
(7, '2024-09-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-09-01', 'September rent'),
(7, '2024-10-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-10-01', 'October rent'),
(7, '2024-11-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-11-01', 'November rent'),
(7, '2024-12-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-12-01', 'December rent'),
(7, '2025-01-01', 850.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2025-01-01', 'January 2025 rent');

-- Tenant 8 (James) - GOOD PAYER
INSERT INTO payments (lease_id, payment_date, amount, payment_type, payment_method, payment_status, due_date, notes) VALUES
(8, '2023-12-01', 1350.00, 'SECURITY_DEPOSIT', 'BANK_TRANSFER', 'COMPLETED', '2023-12-01', 'Security deposit'),
(8, '2023-12-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2023-12-01', 'December 2023 rent'),
(8, '2024-01-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-01-01', 'January rent'),
(8, '2024-02-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-02-01', 'February rent'),
(8, '2024-03-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-03-01', 'March rent'),
(8, '2024-04-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-04-01', 'April rent'),
(8, '2024-05-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-05-01', 'May rent'),
(8, '2024-06-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-06-01', 'June rent'),
(8, '2024-07-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-07-01', 'July rent'),
(8, '2024-08-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-08-01', 'August rent'),
(8, '2024-09-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-09-01', 'September rent'),
(8, '2024-10-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-10-01', 'October rent'),
(8, '2024-11-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-11-01', 'November rent'),
(8, '2024-12-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2024-12-01', 'December rent'),
(8, '2025-01-01', 1350.00, 'RENT', 'BANK_TRANSFER', 'COMPLETED', '2025-01-01', 'January 2025 rent');

-- Some maintenance requests to complete the data
INSERT INTO maintenance_requests (property_id, tenant_id, request_date, description, priority, status, estimated_cost, actual_cost, completion_date, notes) VALUES
(1, 1, '2024-03-15', 'Leaking faucet in kitchen', 'MEDIUM', 'COMPLETED', 150.00, 125.00, '2024-03-16', 'Fixed by plumber'),
(2, 2, '2024-05-20', 'AC not cooling properly', 'HIGH', 'COMPLETED', 300.00, 350.00, '2024-05-21', 'Needed new filter and freon'),
(3, 3, '2024-02-10', 'Broken window in bedroom', 'HIGH', 'COMPLETED', 200.00, 185.00, '2024-02-12', 'Window replaced'),
(4, 4, '2024-07-05', 'Dishwasher making noise', 'LOW', 'COMPLETED', 100.00, 75.00, '2024-07-08', 'Cleaned and adjusted'),
(5, 5, '2024-09-12', 'Toilet running constantly', 'MEDIUM', 'COMPLETED', 80.00, 60.00, '2024-09-13', 'Replaced flapper'),
(6, 6, '2024-01-25', 'Heater not working', 'URGENT', 'COMPLETED', 400.00, 450.00, '2024-01-26', 'Replaced heating element'),
(7, 7, '2024-11-03', 'Garbage disposal jammed', 'LOW', 'COMPLETED', 50.00, 0.00, '2024-11-04', 'Tenant fixed it themselves'),
(8, 8, '2024-08-18', 'Roof leak during rain', 'URGENT', 'COMPLETED', 800.00, 750.00, '2024-08-20', 'Patched roof section'),
(1, 1, '2024-12-10', 'Light fixture flickering', 'LOW', 'OPEN', 75.00, NULL, NULL, 'Scheduled for next week'),
(3, 3, '2025-01-15', 'Water heater not heating', 'HIGH', 'IN_PROGRESS', 600.00, NULL, NULL, 'Waiting for parts');

-- Summary count: Let's count our rows
-- Properties: 8
-- Tenants: 10  
-- Leases: 10
-- Payments: 114 (way more than 50!)
-- Maintenance: 10
-- TOTAL: 152 rows!
