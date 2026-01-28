# 🚀 RENTFLOW - QUICK START GUIDE

## ⚡ 5-Minute Setup

### 1. Install MySQL (if not already installed)

```bash
# Mac:
brew install mysql
brew services start mysql

# Windows: Download from https://dev.mysql.com/downloads/installer/

# Linux (Ubuntu/Debian):
sudo apt-get install mysql-server
sudo systemctl start mysql
```

### 2. Create Database & Load Data

```bash
# Login to MySQL
mysql -u root -p

# Copy and paste these commands:
source schema.sql
source test_data.sql
```

### 3. Verify Data Loaded

```sql
USE rentflow_pm;
SELECT COUNT(*) FROM payments;  -- Should show 114
```

### 4. Update Database Password

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.password=YOUR_MYSQL_ROOT_PASSWORD
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

## 📸 Screenshots Needed for Submission

1. **Database Diagram**
   - MySQL Workbench → Database → Reverse Engineer
   - Shows all 5 tables with relationships

2. **Row Count Proof (50+ rows)**

   ```sql
   SELECT COUNT(*) FROM payments;
   ```

   - Take screenshot showing 114 rows

3. **Application Menu**
   - Screenshot of main menu when app starts

4. **Payment Tracking Feature**
   - Select option 1
   - Shows who owes what

5. **Full Data Retrieval**
   - Select option 5 (View All Payments)
   - Shows the data is accessible

## 🎯 Key Features to Demonstrate

### Payment Tracking (Option 1) - THE STAR FEATURE! ⭐

Shows:

- Property address
- Tenant name
- Monthly rent amount
- Total paid
- Outstanding balance
- Payment status (✅ PAID or ❌ OWES)

### Payment Summary (Option 6)

Shows:

- Total rent received
- Total rent expected
- Collection rate %
- Late payment count

### Overdue Payments (Option 8)

Shows:

- Who's late
- How much they owe
- How many days overdue

## 🐛 Common Issues & Fixes

| Problem                | Solution                                      |
| ---------------------- | --------------------------------------------- |
| Can't connect to MySQL | Check MySQL is running: `mysql.server status` |
| Wrong password         | Update `application.properties`               |
| Database doesn't exist | Run `schema.sql` first                        |
| No data in tables      | Run `test_data.sql`                           |
| Port 3306 in use       | Stop other MySQL instances                    |
| Java version error     | Need Java 17+: `java -version`                |

## 📊 What Makes This Project Great

✅ **5 Tables** - Properties, Tenants, Leases, Payments, Maintenance
✅ **Foreign Keys** - Proper relationships (Leases→Properties, Leases→Tenants, Payments→Leases)
✅ **114 Payment Records** - Way more than the 50 required!
✅ **Real Scenarios** - Perfect payers, late payers, problem tenants
✅ **Full CRUD** - All Create, Read, Update, Delete operations
✅ **Payment Tracking** - The core feature that matters!
✅ **Professional Code** - Spring Boot, JPA, proper structure

## 💡 Pro Tips

1. **For Screenshots**: Make your terminal full screen for better clarity
2. **Database Diagram**: Use colors in MySQL Workbench for visual appeal
3. **Test the Features**: Try all menu options before taking screenshots
4. **Check the Data**: Run `verify_database.sql` to see statistics

## 🎓 Meeting Course Requirements

| Requirement       | Status | Details                    |
| ----------------- | ------ | -------------------------- |
| 3-5 tables        | ✅     | 5 tables total             |
| 50+ rows          | ✅     | 152 rows total             |
| SQL scripts       | ✅     | schema.sql + test_data.sql |
| Data access layer | ✅     | 5 Repository interfaces    |
| CRUD operations   | ✅     | All implemented            |
| Console front-end | ✅     | Full menu system           |
| Data retrieval    | ✅     | Multiple view options      |
| GitHub repo       | ⏳     | You'll do this next!       |

## 🏃 Next Steps

1. **Test the app** - Run through all menu options
2. **Take screenshots** - All 5 required screenshots
3. **Create GitHub repo** - Push this code
4. **Submit to Blackboard** - Screenshots + GitHub link

---

**BREATHE IN THE SUCCESS!** ❄️💪

You now have a professional-grade property management system with real payment tracking capability!
