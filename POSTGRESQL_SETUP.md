# PostgreSQL Setup Guide

## PostgreSQL Database Configuration

This project now supports both MySQL and PostgreSQL databases. Use the appropriate application properties file based on your database choice.

## Available Configuration Files

### 1. MySQL Configuration (Default)
- **File**: `application.properties`
- **Usage**: Default configuration, uses MySQL
- **Database**: `playschool_db`

### 2. PostgreSQL Configuration
- **File**: `application-postgresql.properties`
- **Usage**: For PostgreSQL database
- **Database**: `playschool_db`

### 3. Development with PostgreSQL
- **File**: `application-dev.properties`
- **Usage**: Development environment with PostgreSQL
- **Database**: `playschool_dev_db`

### 4. Production with PostgreSQL
- **File**: `application-prod.properties`
- **Usage**: Production environment with PostgreSQL
- **Database**: `playschool_prod_db`

## PostgreSQL Setup Instructions

### Step 1: Install PostgreSQL
```bash
# Windows (using Chocolatey)
choco install postgresql

# Or download from: https://www.postgresql.org/download/windows/
```

### Step 2: Create Database
```sql
-- Connect to PostgreSQL as superuser
psql -U postgres

-- Create database
CREATE DATABASE playschool_db;

-- For development
CREATE DATABASE playschool_dev_db;

-- For production
CREATE DATABASE playschool_prod_db;

-- Create user (optional)
CREATE USER playschool_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE playschool_db TO playschool_user;
```

### Step 3: Update Configuration
Update the password in your chosen properties file:
```properties
spring.datasource.password=your_actual_password
```

## Running with Different Profiles

### Option 1: Using Spring Profiles
```bash
# Run with PostgreSQL profile
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql

# Run with development profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run with production profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Option 2: Using Specific Properties File
```bash
# Run with specific config file
mvn spring-boot:run -Dspring.config.location=classpath:/application-postgresql.properties
```

### Option 3: Set Active Profile in application.properties
Add this line to your main `application.properties`:
```properties
spring.profiles.active=postgresql
```

## Configuration Differences

### MySQL vs PostgreSQL
| Feature | MySQL | PostgreSQL |
|---------|--------|------------|
| **Driver** | `com.mysql.cj.jdbc.Driver` | `org.postgresql.Driver` |
| **URL Format** | `jdbc:mysql://localhost:3306/db` | `jdbc:postgresql://localhost:5432/db` |
| **Dialect** | `MySQL8Dialect` | `PostgreSQLDialect` |
| **Default Port** | 3306 | 5432 |

### Environment-Specific Settings

#### Development
- `ddl-auto=create-drop` (recreates schema)
- `show-sql=true` (shows SQL queries)
- More verbose logging

#### Production
- `ddl-auto=validate` (validates schema)
- `show-sql=false` (no SQL logging)
- Environment variables for sensitive data
- Connection pooling optimized

## Testing the Connection

### 1. Start PostgreSQL Service
```bash
# Windows
net start postgresql-x64-13

# Or using services.msc
```

### 2. Verify Connection
```bash
psql -U postgres -d playschool_db
```

### 3. Run the Application
```bash
# With PostgreSQL profile
mvn spring-boot:run -Dspring.profiles.active=postgresql
```

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Ensure PostgreSQL service is running
   - Check if port 5432 is available

2. **Authentication Failed**
   - Verify username and password
   - Check PostgreSQL `pg_hba.conf` settings

3. **Database Does Not Exist**
   - Create the database manually using psql
   - Or set `createDatabaseIfNotExist=true` in URL (not supported in PostgreSQL)

### Environment Variables (Production)
```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret
export ALLOWED_ORIGINS=https://yourdomain.com
```

## Switching Between Databases

To switch from MySQL to PostgreSQL:

1. **Stop the application**
2. **Change active profile**:
   ```properties
   spring.profiles.active=postgresql
   ```
3. **Update database credentials**
4. **Restart the application**

The JPA entities and repositories remain the same - only the database connection changes!
