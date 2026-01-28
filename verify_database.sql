-- RentFlow Database Verification Script
-- Run this to verify your database is set up correctly!

USE rentflow_pm;

-- Check table counts
SELECT 'Properties' as Table_Name, COUNT(*) as Row_Count FROM properties
UNION ALL
SELECT 'Tenants', COUNT(*) FROM tenants
UNION ALL
SELECT 'Leases', COUNT(*) FROM leases
UNION ALL
SELECT 'Payments', COUNT(*) FROM payments
UNION ALL
SELECT 'Maintenance Requests', COUNT(*) FROM maintenance_requests;

-- Verify we have 50+ payment rows
SELECT 
    CASE 
        WHEN COUNT(*) >= 50 THEN '✅ PASS: 50+ payment rows'
        ELSE '❌ FAIL: Less than 50 payment rows'
    END as Requirement_Check,
    COUNT(*) as Actual_Count
FROM payments;

-- Check for foreign key relationships
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'rentflow_pm'
    AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, COLUMN_NAME;

-- Quick payment tracking preview
SELECT 
    CONCAT(t.first_name, ' ', t.last_name) as Tenant,
    p.address as Property,
    l.monthly_rent as Monthly_Rent,
    COALESCE(SUM(CASE WHEN pay.payment_status = 'COMPLETED' THEN pay.amount ELSE 0 END), 0) as Total_Paid,
    COALESCE(SUM(CASE WHEN pay.payment_status = 'PENDING' AND pay.due_date < CURDATE() THEN pay.amount ELSE 0 END), 0) as Outstanding
FROM leases l
JOIN tenants t ON l.tenant_id = t.tenant_id
JOIN properties p ON l.property_id = p.property_id
LEFT JOIN payments pay ON l.lease_id = pay.lease_id
WHERE l.lease_status = 'ACTIVE'
GROUP BY l.lease_id, t.first_name, t.last_name, p.address, l.monthly_rent
ORDER BY Outstanding DESC;

-- Summary Statistics
SELECT 
    'Total Properties' as Metric, COUNT(*) as Value FROM properties
UNION ALL
SELECT 'Total Tenants', COUNT(*) FROM tenants
UNION ALL
SELECT 'Active Leases', COUNT(*) FROM leases WHERE lease_status = 'ACTIVE'
UNION ALL
SELECT 'Total Payments', COUNT(*) FROM payments
UNION ALL
SELECT 'Completed Payments', COUNT(*) FROM payments WHERE payment_status = 'COMPLETED'
UNION ALL
SELECT 'Pending Payments', COUNT(*) FROM payments WHERE payment_status = 'PENDING';

-- Show who owes money (for screenshot!)
SELECT 
    CONCAT(t.first_name, ' ', t.last_name) as 'Tenant Name',
    p.address as 'Property Address',
    SUM(CASE WHEN pay.payment_status = 'PENDING' AND pay.due_date < CURDATE() THEN pay.amount ELSE 0 END) as 'Amount Owed'
FROM leases l
JOIN tenants t ON l.tenant_id = t.tenant_id
JOIN properties p ON l.property_id = p.property_id
LEFT JOIN payments pay ON l.lease_id = pay.lease_id
WHERE l.lease_status = 'ACTIVE'
GROUP BY t.tenant_id, t.first_name, t.last_name, p.address
HAVING SUM(CASE WHEN pay.payment_status = 'PENDING' AND pay.due_date < CURDATE() THEN pay.amount ELSE 0 END) > 0
ORDER BY `Amount Owed` DESC;
