# ❄️ RentFlow - Professional Property Management System ❄️

**Like ice, your data is crystal clear and solid!**

A professional property management system designed for small landlords (5-10 properties) with a focus on **payment tracking** - know exactly who owes what, who's paid up, and who's falling behind.

## 🎯 Project Purpose

This is a full-stack Spring Boot + React application created for CSCE 548. It demonstrates:

- Professional-grade database design with proper relationships
- Complete CRUD operations using Spring Data JPA
- Real-world data scenarios (114 payment records!)
- REST API services consumed by a modern React client
- Full two-way CRUD from client → API → database

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
- Node.js 20+ (for the React client)
- MySQL 8.0+
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

### Step 4: Build and Run (API Only)

```bash
# Navigate to project directory
cd rentflow-property-manager

# Build the project
mvn clean install

# Run the API (Tomcat on 8080)
mvn spring-boot:run
```

### Step 5: Run Full Stack (API + Client)

From the project root:

```bash
npm install
npm run dev
```

## 🎮 Using the Application

The **React client** replaces the console UI and provides:

### Client Features

- Dashboard with quick “Load All” actions
- Separate pages for Properties, Tenants, Leases, Payments, Maintenance
- Create / Update / Delete for all entities
- Full **GET** coverage for all endpoints (single, list, and filtered)
- Modal popups for updates (no clunky inline forms)
- Aesthetic card grids for all data lists

## 🗂️ Project Structure

```
RentFlow/
├── pom.xml                          # Maven configuration
├── README.md                        # Project documentation
├── QUICK_START.md                   # Short setup guide
├── PROJECT_SUMMARY.md               # Project overview
├── package.json                      # Root dev runner (API + Client)
├── client/                           # React client (Vite)
│   ├── src/
│   ├── vite.config.js
│   └── package.json
├── schema.sql                       # Database creation script
├── test_data.sql                    # 114 payment records + more
├── verify_database.sql              # Verification queries
├── src/
│   └── main/
│       ├── java/com/rentflow/
│       │   ├── RentFlowApplication.java      # Spring Boot entry point
│       │   ├── controller/                   # REST API layer
│       │   │   ├── LeaseController.java
│       │   │   ├── MaintenanceRequestController.java
│       │   │   ├── PaymentController.java
│       │   │   ├── PropertyController.java
│       │   │   └── TenantController.java
│       │   ├── model/                        # Entity + enum types
│       │   │   ├── EmploymentStatus.java
│       │   │   ├── Lease.java
│       │   │   ├── LeaseStatus.java
│       │   │   ├── MaintenanceRequest.java
│       │   │   ├── MaintenanceStatus.java
│       │   │   ├── Payment.java
│       │   │   ├── PaymentMethod.java
│       │   │   ├── PaymentStatus.java
│       │   │   ├── PaymentType.java
│       │   │   ├── Priority.java
│       │   │   ├── Property.java
│       │   │   ├── PropertyType.java
│       │   │   └── Tenant.java
│       │   ├── repository/                  # Data access layer (CRUD)
│       │   │   ├── LeaseRepository.java
│       │   │   ├── MaintenanceRequestRepository.java
│       │   │   ├── PaymentRepository.java
│       │   │   ├── PropertyRepository.java
│       │   │   └── TenantRepository.java
│       │   └── service/                      # Business layer
│       │       ├── LeaseService.java
│       │       ├── MaintenanceRequestService.java
│       │       ├── PaymentService.java
│       │       ├── PropertyService.java
│       │       └── TenantService.java
│       └── resources/
│           └── application.properties        # Database configuration
└── target/                                   # Build outputs
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

## 🌐 REST API (Service Layer)

The application exposes REST endpoints under `/api`.

**Core resources (CRUD):**

- `GET /api/properties` (list)
- `GET /api/properties/{id}` (read)
- `POST /api/properties` (create)
- `PUT /api/properties/{id}` (update)
- `DELETE /api/properties/{id}` (delete)

Equivalent CRUD endpoints exist for `/api/tenants`, `/api/leases`, `/api/payments`, and `/api/maintenance-requests`.

## 🖥️ React Client (Required for Project 3)

- URL: http://localhost:5173
- API: http://localhost:8080
- The client calls **all GET endpoints** (single, list, filters) and supports full CRUD.

## ☁️ Hosting (Example: Render)

The service can be hosted on a platform like Render using:

- **Build command:** `mvn clean package`
- **Start command:** `java -jar target/rentflow-property-manager-1.0.0.jar`
- **Environment variables:**
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`

This hosts the REST services under `/api/*` on port 8080.

## 📚 Technologies Used

- **Java 17** - Backend language
- **Spring Boot 3.2.1** - REST API framework
- **Spring Data JPA + Hibernate** - ORM
- **MySQL 8.0** - Database
- **Maven** - Backend build tool
- **React + Vite** - Client application
- **Node.js 20+** - Frontend tooling

## 📝 License

Created for CSCE 548 - University of South Carolina

---

_Stay cold, stay focused, stay profitable!_ 💼❄️
