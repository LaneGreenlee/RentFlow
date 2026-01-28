# ❄️ RentFlow - Professional Property Management System ❄️

**Like ice, your data is crystal clear and solid!**

A professional property management system designed for small landlords (5-10 properties) with a focus on **payment tracking** - know exactly who owes what, who's paid up, and who's falling behind.

## 🎯 Project Purpose

This is a fully-functional Spring Boot application created for CSCE 548. It demonstrates:

- Professional-grade database design with proper relationships
- Complete CRUD operations using Spring Data JPA
- Real-world data scenarios (114 payment records!)
- Console-based interface for data interaction

## 🏗️ Database Structure

**5 Tables with Proper Relationships:**

1. **Properties** - Your rental properties
2. **Tenants** - The people renting from you
3. **Leases** - Contracts connecting properties and tenants (Foreign Keys!)
4. **Payments** - THE HEART! Track every dollar (Foreign Key to Leases)
5. **Maintenance Requests** - Track repairs (Foreign Keys to Properties & Tenants)

## 📊 Test Data Highlights

The database includes **114 payment records** showing realistic scenarios:

- ✅ Perfect payers (Sarah, David, Amanda, James)
- ⚠️ Occasionally late (Michael, Jessica)
- 🚨 Problem tenant (Christopher - owes $3,900!)
- 📉 Spotty payment history (Emily - missing multiple months)

**Total: 152 rows across all tables**

## 🚀 Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (or PostgreSQL - see instructions below)
- Git

### Step 1: Install MySQL

**Download MySQL Community Server:**

- Visit: https://www.mysql.com/downloads/
- Choose "MySQL Community Server"
- Install and remember your root password!

**Start MySQL:**

```bash
# On Mac with Homebrew:
brew services start mysql

# On Windows:
# Use MySQL Workbench or Services panel

# On Linux:
sudo systemctl start mysql
```

### Step 2: Create the Database

```bash
# Login to MySQL
mysql -u root -p

# Run the schema script
source /path/to/schema.sql

# Run the test data script
source /path/to/test_data.sql

# Verify data loaded
USE rentflow_pm;
SELECT COUNT(*) FROM payments;  -- Should show 114
SELECT COUNT(*) FROM properties;  -- Should show 8
```

**Alternative: Run scripts directly**

```bash
mysql -u root -p < schema.sql
mysql -u root -p < test_data.sql
```

### Step 3: Configure the Application

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_ACTUAL_PASSWORD_HERE
```

### Step 4: Build and Run

```bash
# Navigate to project directory
cd rentflow-property-manager

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## 🎮 Using the Application

When you run the app, you'll see a professional menu:

```
======================================================================
❄️  RENTFLOW - PROFESSIONAL PROPERTY MANAGEMENT SYSTEM  ❄️
    Like ice, your data is crystal clear and solid!
======================================================================

MAIN MENU
======================================================================
1. 💰 PAYMENT TRACKING - Who Owes What
2. 🏠 View All Properties
3. 👥 View All Tenants
4. 📄 View All Leases
5. 💵 View All Payments
6. 📊 Payment Summary Report
7. 🔧 View Maintenance Requests
8. ⚠️  View Overdue Payments
9. 🚪 Exit
======================================================================
```

### Key Features:

**Option 1: Payment Tracking** 

- See who owes money
- See who's paid up
- Track outstanding balances per tenant

**Option 6: Payment Summary Report**

- Total rent received
- Total rent expected
- Collection rate percentage
- Count of late payments

**Option 8: Overdue Payments**

- See exactly which payments are overdue
- How many days overdue
- Amount owed

## 🗂️ Project Structure

```
rentflow-property-manager/
├── pom.xml                          # Maven configuration
├── schema.sql                       # Database creation script
├── test_data.sql                    # 114 payment records + more
├── src/
│   └── main/
│       ├── java/com/rentflow/
│       │   ├── RentFlowApplication.java    # Main console app
│       │   ├── model/                       # Entity classes
│       │   │   ├── Property.java
│       │   │   ├── Tenant.java
│       │   │   ├── Lease.java
│       │   │   ├── Payment.java
│       │   │   └── MaintenanceRequest.java
│       │   └── repository/                  # Data access layer (CRUD)
│       │       ├── PropertyRepository.java
│       │       ├── TenantRepository.java
│       │       ├── LeaseRepository.java
│       │       ├── PaymentRepository.java
│       │       └── MaintenanceRequestRepository.java
│       └── resources/
│           └── application.properties       # Database configuration
```

## 🔧 Troubleshooting

### "Access denied for user 'root'@'localhost'"

- Update the password in `application.properties`
- Make sure MySQL is running

### "Unknown database 'rentflow_pm'"

- Run the schema.sql script first
- Check that you're connected to MySQL

### "Table 'rentflow_pm.payments' doesn't exist"

- Run the schema.sql script
- Check `spring.jpa.hibernate.ddl-auto=update` in application.properties

### Port 3306 already in use

- Another MySQL instance is running
- Change the port in application.properties

## 📚 Technologies Used

- **Java 17** - Modern, professional Java
- **Spring Boot 3.2.1** - Industry-standard framework
- **Spring Data JPA** - Elegant data access
- **Hibernate** - ORM for database interaction
- **MySQL 8.0** - Robust relational database
- **Maven** - Dependency management

## 📝 License

Created for CSCE 548 - University of South Carolina

---

_Stay cold, stay focused, stay profitable!_ 💼❄️
