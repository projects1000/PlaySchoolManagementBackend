
# PlaySchool Management Backend

A comprehensive Spring Boot backend application for managing play school operations including student management, user authentication, and role-based access control.

## Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Student Management**: CRUD operations for student records
- **Role Management**: Support for Admin, Teacher, Parent, and Staff roles
- **Security**: Spring Security integration with password encryption
- **Database**: MySQL/PostgreSQL (Production), H2 (Testing)
- **RESTful APIs**: Well-structured REST endpoints
- **Data Validation**: Input validation using Bean Validation

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Security**: Spring Security with JWT
- **Database**: Spring Data JPA/Hibernate with MySQL/PostgreSQL support
- **ORM**: Spring Data JPA/Hibernate
- **Validation**: Spring Boot Validation
- **Build Tool**: Maven
- **Java Version**: 17

## Project Structure

```
src/
├── main/
│   ├── java/com/playschool/management/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Data repositories
│   │   └── security/       # Security configurations
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── resources/
            └── application-test.properties
```

## API Endpoints

### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration

### Students (Protected)
- `GET /api/students` - Get all active students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Create new student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Soft delete student
- `GET /api/students/search?name={name}` - Search students by name

### Test Endpoints
- `GET /api/test/all` - Public access
- `GET /api/test/admin` - Admin only
- `GET /api/test/teacher` - Teacher access
- `GET /api/test/parent` - Parent access
- `GET /api/test/staff` - Staff access

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ OR PostgreSQL 12+ (for production)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PlaySchoolManagementBackend
   ```

2. **Configure Database**
   
   **For MySQL** (update `src/main/resources/application.properties`):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/playschool_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
   
   **For PostgreSQL** (use `application-postgresql.properties` or set profile):
   ```properties
   spring.profiles.active=postgresql
   # OR
   spring.datasource.url=jdbc:postgresql://localhost:5432/playschool_db
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

3. **Create Database**
   
   **For MySQL:**
   ```sql
   CREATE DATABASE playschool_db;
   ```
   
   **For PostgreSQL:**
   ```sql
   CREATE DATABASE playschool_db;
   # Connect: psql -U postgres
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   - API Base URL: `http://localhost:8080/api`
   - H2 Console (testing): `http://localhost:8080/h2-console`

## Default Configuration

### Database Settings
- **Production**: MySQL on port 3306
- **Testing**: H2 in-memory database
- **JPA**: Auto DDL update enabled

### Security Settings
- **JWT Secret**: Configurable via `jwt.secret`
- **JWT Expiration**: 24 hours (86400000ms)
- **Password Encoding**: BCrypt

### CORS Settings
- **Allowed Origins**: `http://localhost:3000`, `http://localhost:3001`
- **Allowed Methods**: GET, POST, PUT, DELETE, PATCH, OPTIONS

## User Roles

1. **ADMIN**: Full system access
2. **TEACHER**: Student management, read access
3. **STAFF**: Student management (limited)
4. **PARENT**: Limited access to own child's information

## Testing

Run tests with:
```bash
mvn test
```

Tests use H2 in-memory database with test-specific configuration.

## Development

### Adding New Features

1. **Entity**: Create JPA entity in `entity/` package
2. **Repository**: Create repository interface in `repository/`
3. **DTO**: Create request/response DTOs in `dto/`
4. **Controller**: Create REST controller in `controller/`
5. **Service**: Add business logic in `service/` (if needed)

### Security Configuration

The application uses JWT tokens for authentication. To access protected endpoints:

1. Login via `/api/auth/signin`
2. Include JWT token in Authorization header: `Bearer <token>`

## Production Deployment

1. **Update Configuration**: Set production database credentials
2. **Environment Variables**: Use environment variables for sensitive data
3. **Profile**: Use Spring profiles for different environments
4. **SSL**: Configure HTTPS for production
5. **Remove H2 Console**: Disable H2 console access

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes and add tests
4. Submit a pull request

## License

This project is licensed under the MIT License.
=======
# PlaySchoolManagementBackend

