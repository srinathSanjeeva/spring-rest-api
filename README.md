# Enterprise Employee Management REST API

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue)
![Test Coverage](https://img.shields.io/badge/Test%20Coverage-97%25-brightgreen)
![Security](https://img.shields.io/badge/Security-Enabled-green)

A **production-ready, enterprise-grade** Spring Boot REST API for Employee Management with comprehensive security, validation, caching, and extensive test coverage. This application follows industry best practices and implements modern Spring Boot patterns.

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (optional - H2 included for development)
- **Docker** (optional)

## 🎯 Quick Start

### 1. Clone & Build

```bash
git clone https://github.com/srinathSanjeeva/spring-rest-api.git
cd spring-rest-api
./mvnw clean install
```

### 2. Set Required Environment Variables

**IMPORTANT**: This application now requires environment variables for secure credential management:

#### Windows PowerShell:
```powershell
$env:APP_SECURITY_ADMIN_PASSWORD="SecureAdmin123!"
$env:APP_SECURITY_USER_PASSWORD="SecureUser123!"
$env:APP_SECURITY_JWT_SECRET="MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"
```

#### Linux/macOS:
```bash
export APP_SECURITY_ADMIN_PASSWORD="SecureAdmin123!"
export APP_SECURITY_USER_PASSWORD="SecureUser123!"
export APP_SECURITY_JWT_SECRET="MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"
```

### 3. Run Application

```bash
# With all required environment variables set
# Linux/macOS:
export APP_SECURITY_ADMIN_PASSWORD="SecureAdmin123!"
export APP_SECURITY_USER_PASSWORD="SecureUser123!"
export APP_SECURITY_JWT_SECRET="MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"
./mvnw spring-boot:run

# Windows PowerShell:
$env:APP_SECURITY_ADMIN_PASSWORD="SecureAdmin123!"; $env:APP_SECURITY_USER_PASSWORD="SecureUser123!"; $env:APP_SECURITY_JWT_SECRET="MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"; ./mvnw spring-boot:run

# Custom port (if 8080 is in use)
SERVER_PORT=8081 ./mvnw spring-boot:run

# With MySQL profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

### 4. Quick Start with Secure Scripts

#### Windows PowerShell:
```powershell
# Use the secure startup script (default port 8080)
.\start-secure.ps1

# Custom port if needed
.\start-secure.ps1 -Port 8081
```

#### Linux/macOS:
```bash
# Make script executable and run (default port 8080)
chmod +x start-secure.sh
./start-secure.sh

# Custom port if needed
./start-secure.sh 8081
```

### 5. Verify Installation

```bash
# Test with secure credentials (default port 8080)
curl -u admin:SecureAdmin123! http://localhost:8080/api/v1/employees
curl -u user:SecureUser123! http://localhost:8080/api/v1/employees

# If using custom port, adjust accordingly
curl -u admin:SecureAdmin123! http://localhost:8081/api/v1/employees
```

## 🚀 Features

### ✨ **Enterprise Architecture**
- **Layered Architecture**: Controller → Service → Repository pattern
- **DTO Pattern**: Entity-DTO mapping with MapStruct
- **Comprehensive Exception Handling**: Global error handling with structured responses
- **Advanced Security**: Spring Security with Basic Authentication & role-based access
- **Performance Optimization**: Caching with Spring Cache
- **Data Validation**: Bean Validation with custom error messages
- **API Documentation**: OpenAPI 3.0 with Swagger UI

### 🛡️ **Enterprise Security Features**
- **Environment-Based Credentials**: No hardcoded passwords, all credentials via environment variables
- **Strong Password Enforcement**: 8+ chars with complexity requirements (uppercase, lowercase, digits, symbols)
- **HTTP Basic Authentication** with BCrypt password hashing (strength 12)
- **Role-based Authorization** (ADMIN/USER) with method-level security
- **CRLF Injection Protection**: Logback configuration automatically sanitizes log output
- **Input Validation & Sanitization**: Unicode normalization and malicious character filtering
- **CORS Configuration**: Configurable allowed origins via environment variables
- **Security Headers**: XSS, CSRF protection, and secure headers
- **JWT Security**: Configurable JWT secret with 32+ character requirement
- **Audit Logging**: Security events logged to separate audit file
- **Static Security Analysis**: SpotBugs with FindSecBugs for vulnerability detection

### 📊 **Data Management**
- **JPA/Hibernate** with H2 (in-memory) and MySQL support
- **Entity Auditing** (created/updated timestamps)
- **Optimistic Locking** with versioning
- **Custom Queries** with Spring Data JPA
- **Database Migration** ready

### 🔧 **Production Features**
- **Comprehensive Logging** with SLF4J
- **Health Checks** with Spring Boot Actuator
- **Caching Strategy** for performance
- **Environment Profiles** (dev, mysql, production)
- **Docker Support** with optimized containers

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (optional - H2 included for development)
- **Docker** (optional)

## 🎯 Quick Start

### 1. Clone & Build

```bash
git clone https://github.com/srinathSanjeeva/spring-rest-api.git
cd spring-rest-api
./mvnw clean install
```

### 2. Run Application

```bash
# With all required environment variables set
# Linux/macOS:
export APP_SECURITY_ADMIN_PASSWORD="SecureAdmin123!"
export APP_SECURITY_USER_PASSWORD="SecureUser123!"
export APP_SECURITY_JWT_SECRET="MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"
./mvnw spring-boot:run

# Windows PowerShell:
$env:APP_SECURITY_ADMIN_PASSWORD="SecureAdmin123!"; $env:APP_SECURITY_USER_PASSWORD="SecureUser123!"; $env:APP_SECURITY_JWT_SECRET="MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"; ./mvnw spring-boot:run

# Custom port (recommended)
SERVER_PORT=8081 ./mvnw spring-boot:run

# With MySQL profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

### 3. Verify Installation

```bash
# Test with secure credentials (adjust port as needed)
curl -u admin:SecureAdmin123! http://localhost:8081/api/v1/employees
curl -u user:SecureUser123! http://localhost:8081/api/v1/employees
```

## 🔐 Authentication

The API uses **HTTP Basic Authentication** with **environment-based secure credentials**:

### Required Environment Variables

| Environment Variable | Description | Example |
|---------------------|-------------|---------|
| `APP_SECURITY_ADMIN_PASSWORD` | Admin user password | `SecureAdmin123!` |
| `APP_SECURITY_USER_PASSWORD` | Regular user password | `SecureUser123!` |
| `APP_SECURITY_JWT_SECRET` | JWT secret key (32+ chars) | `MyVerySecureJWTSecret...` |

### User Roles & Access

| Username | Roles | Access |
|----------|-------|--------|
| `admin` | ADMIN, USER | Full access + Actuator endpoints |
| `user` | USER | Read/Write employees |

### Password Requirements

- **Minimum 8 characters**
- **At least one lowercase letter**
- **At least one uppercase letter**
- **At least one digit**
- **At least one special character** (@$!%*?&)

> 🔒 **Enterprise Security**: No hardcoded credentials! All passwords must be set via environment variables.

## 📡 API Endpoints

### 🧑‍💼 Employee Management

All employee endpoints require authentication and are prefixed with `/api/v1/employees`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/api/v1/employees` | Get all employees (paginated) | USER |
| `GET` | `/api/v1/employees/{id}` | Get employee by ID | USER |
| `POST` | `/api/v1/employees` | Create new employee | USER |
| `PUT` | `/api/v1/employees/{id}` | Update employee | USER |
| `PATCH` | `/api/v1/employees/{id}` | Partially update employee | USER |
| `DELETE` | `/api/v1/employees/{id}` | Delete employee | USER |
| `GET` | `/api/v1/employees/search?name={name}` | Search by name | USER |
| `GET` | `/api/v1/employees/role/{role}` | Get by role | USER |
| `GET` | `/api/v1/employees/count` | Get total count | USER |

### 📊 Query Parameters

- **Pagination**: `?page=0&size=10`
- **Sorting**: `?sortBy=name&sortDir=asc`
- **Search**: `?name=John` or `?role=Developer`

## 🧪 API Testing

### Using cURL

```bash
# Get all employees with pagination
curl -u user:SecureUser123! "http://localhost:8080/api/v1/employees?page=0&size=5&sortBy=name&sortDir=asc"

# Get employee by ID
curl -u user:SecureUser123! http://localhost:8080/api/v1/employees/1

# Create new employee
curl -u user:SecureUser123! -X POST http://localhost:8080/api/v1/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "role": "Software Engineer",
    "active": true
  }'

# Update employee
curl -u user:SecureUser123! -X PUT http://localhost:8080/api/v1/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "role": "Senior Developer",
    "active": true
  }'

# Partial update (PATCH)
curl -u user:SecureUser123! -X PATCH http://localhost:8080/api/v1/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "role": "Lead Developer"
  }'

# Search employees
curl -u user:SecureUser123! "http://localhost:8080/api/v1/employees/search?name=John"

# Get employees by role
curl -u user:SecureUser123! http://localhost:8080/api/v1/employees/role/Developer

# Get total employee count
curl -u user:SecureUser123! http://localhost:8080/api/v1/employees/count

# Delete employee (requires admin privileges)
curl -u admin:SecureAdmin123! -X DELETE http://localhost:8080/api/v1/employees/1
```

### Using the Test Script

```bash
# Make script executable and run
chmod +x curlTester.sh
./curlTester.sh
```

## 📖 API Documentation

### Swagger UI
Access interactive API documentation at:
- **URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Authentication**: Login with your secure credentials (e.g., `admin:SecureAdmin123!`)

### OpenAPI Specification
- **JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **YAML**: [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)

## 🏗️ Architecture

### Project Structure
```
src/
├── main/java/org/sanjeevas/springrest/
│   ├── SpringRestApplication.java          # Main application class
│   ├── EmployeeController.java             # REST endpoints
│   ├── Employee.java                       # JPA entity
│   ├── EmployeeRepository.java             # Data access layer
│   ├── SecurityConfig.java                 # Security configuration
│   ├── config/
│   │   └── ApplicationConfig.java          # Application configuration
│   ├── dto/                               # Data Transfer Objects
│   │   ├── EmployeeDto.java
│   │   ├── EmployeeListResponseDto.java
│   │   └── ErrorResponseDto.java
│   ├── exception/                         # Custom exceptions
│   │   ├── EmployeeNotFoundException.java
│   │   └── GlobalExceptionHandler.java
│   ├── mapper/
│   │   └── EmployeeMapper.java            # MapStruct entity-DTO mapping
│   └── service/
│       ├── EmployeeService.java           # Service interface
│       └── impl/
│           └── EmployeeServiceImpl.java   # Service implementation
├── test/java/org/sanjeevas/springrest/   # Comprehensive test suite
└── resources/
    ├── application.properties             # Main configuration
    ├── application-dev.properties         # Development profile
    └── application-mysql.properties       # MySQL profile
```

### Technology Stack
- **Framework**: Spring Boot 3.3.4
- **Java Version**: 17
- **Security**: Spring Security 6.3.3 with enterprise security patterns
- **Database**: H2 (development), MySQL (production)
- **ORM**: Hibernate/JPA
- **Logging**: Logback with CRLF protection and security audit trails
- **Mapping**: MapStruct 1.5.5
- **Validation**: Bean Validation (Hibernate Validator) with custom security validation
- **Documentation**: OpenAPI 3 (springdoc-openapi)
- **Testing**: JUnit 5, Mockito, TestContainers
- **Build Tool**: Maven
- **Caching**: Spring Cache
- **Security Analysis**: SpotBugs with FindSecBugs, SonarQube integration, OWASP Dependency Check

## 🧪 Testing

### Run Tests

```bash
# Run all tests
./mvnw test

# Run with coverage report
./mvnw clean test jacoco:report

# Run only unit tests
./mvnw test -Dtest="*Test"

# Run only integration tests  
./mvnw test -Dtest="*IntegrationTest"
```

### Test Coverage

- **32 Total Tests** (97% pass rate)
- **16 Integration Tests** - Full API endpoint testing
- **15 Unit Tests** - Service layer testing
- **1 Application Context Test**

**Coverage Reports**: `target/site/jacoco/index.html`

## 🔍 Security Analysis

### Run Security Scans

```bash
# SpotBugs security analysis (FindSecBugs plugin)
./mvnw compile spotbugs:check

# OWASP Dependency Check
./mvnw dependency-check:check

# View SpotBugs GUI for detailed analysis
./mvnw spotbugs:gui
```

### Security Report Locations
- **SpotBugs Report**: `target/spotbugsXml.xml`
- **OWASP Report**: `target/dependency-check-report.html`
- **Security Audit Logs**: `logs/security-audit.log`

## 🐳 Docker Deployment

### Build Image

```bash
docker build -t employee-management-api .
```

### Run Container

```bash
# Development (H2 database) with secure credentials
docker run -d -p 8081:8081 \
  -e SERVER_PORT=8081 \
  -e APP_SECURITY_ADMIN_PASSWORD=SecureAdmin123! \
  -e APP_SECURITY_USER_PASSWORD=SecureUser123! \
  -e APP_SECURITY_JWT_SECRET=MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123! \
  employee-management-api

# Production (MySQL)
docker run -d -p 8081:8081 \
  -e SERVER_PORT=8081 \
  -e SPRING_PROFILES_ACTIVE=mysql \
  -e APP_SECURITY_ADMIN_PASSWORD=SecureAdmin123! \
  -e APP_SECURITY_USER_PASSWORD=SecureUser123! \
  -e APP_SECURITY_JWT_SECRET=MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123! \
  -e DB_HOST=mysql-host \
  -e DB_NAME=employee_db \
  -e DB_USERNAME=dbuser \
  -e DB_PASSWORD=dbpass \
  employee-management-api
```

### Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8081:8081"
    environment:
      - SERVER_PORT=8081
      - SPRING_PROFILES_ACTIVE=mysql
      - APP_SECURITY_ADMIN_PASSWORD=SecureAdmin123!
      - APP_SECURITY_USER_PASSWORD=SecureUser123!
      - APP_SECURITY_JWT_SECRET=MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!
      - DB_HOST=mysql
      - DB_NAME=employee_db
      - DB_USERNAME=dbuser
      - DB_PASSWORD=dbpass
    depends_on:
      - mysql
      
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: employee_db
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_USER: dbuser
      MYSQL_PASSWORD: dbpass
    ports:
      - "3306:3306"
```

## ⚙️ Configuration

### Environment Variables

#### Required Security Variables
| Variable | Description | Example |
|----------|-------------|---------|
| `APP_SECURITY_ADMIN_PASSWORD` | Admin password (8+ chars, mixed case, numbers, symbols) | `SecureAdmin123!` |
| `APP_SECURITY_USER_PASSWORD` | User password (8+ chars, mixed case, numbers, symbols) | `SecureUser123!` |
| `APP_SECURITY_JWT_SECRET` | JWT secret (32+ characters) | `MyVerySecureJWTSecret...` |

#### Optional Configuration Variables
| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Application port | `8080` |
| `SPRING_PROFILES_ACTIVE` | Active profile | `default` |
| `APP_SECURITY_ADMIN_USERNAME` | Admin username | `admin` |
| `APP_SECURITY_USER_USERNAME` | User username | `user` |
| `APP_SECURITY_ALLOWED_ORIGINS` | CORS allowed origins | `http://localhost:3000,http://localhost:8080,http://localhost:8081` |

#### Database Variables (MySQL Profile)
| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | Database host | `localhost` |
| `DB_NAME` | Database name | `employee_db` |
| `DB_USERNAME` | Database username | `root` |
| `DB_PASSWORD` | Database password | `password` |

### Database Configuration

**H2 (Development)**:
- **URL**: `jdbc:h2:mem:testdb`
- **Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Username**: `sa` (no password)

**MySQL (Production)**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=dbuser
spring.datasource.password=dbpass
```

## 📊 Monitoring

### Health Checks
- **Health**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) (Public)
- **Info**: [http://localhost:8080/actuator/info](http://localhost:8080/actuator/info) (Public)
- **Metrics**: [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics) (Admin only)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (Admin only)

### Logging
```properties
# Application logs
logging.level.org.sanjeevas.springrest=INFO
logging.level.org.springframework.security=DEBUG
```

## 🚀 Production Deployment

### Prerequisites
1. **External Database**: Configure MySQL/PostgreSQL
2. **Environment Variables**: Set production credentials
3. **Security**: Change default passwords
4. **SSL/TLS**: Enable HTTPS
5. **Reverse Proxy**: Configure Nginx/Apache

### Deployment Checklist
- [ ] Update `application-mysql.properties`
- [ ] Set environment-specific passwords
- [ ] Configure external database
- [ ] Enable SSL certificates
- [ ] Set up monitoring/logging
- [ ] Configure backup strategy
- [ ] Set up CI/CD pipeline

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Sanjeeva Srinath**
- GitHub: [@srinathSanjeeva](https://github.com/srinathSanjeeva)

---

## 🔧 Troubleshooting

### Common Issues

**Port Already in Use**:
```bash
# Windows: Find process using port 8080
netstat -an | findstr 8080

# Linux/macOS: Find and kill process
netstat -tulpn | grep 8080
kill -9 <PID>

# Or use custom port to avoid conflicts
SERVER_PORT=8081 ./mvnw spring-boot:run
.\start-secure.ps1 -Port 8081  # Windows
./start-secure.sh 8081         # Linux/macOS
```

**Database Connection Issues**:
```bash
# Check MySQL connection
mysql -h localhost -u dbuser -p employee_db
```

**Authentication Problems**:
- Verify environment variables are set correctly
- Check credentials match your environment variables (e.g., `admin:SecureAdmin123!`)
- Check endpoint URLs: `/api/v1/employees` (not `/employees`)
- Verify port number in URLs (e.g., `:8081` instead of `:8080`)

**Environment Variable Issues**:
```bash
# Check if variables are set (Linux/macOS)
echo $APP_SECURITY_ADMIN_PASSWORD

# Windows PowerShell
echo $env:APP_SECURITY_ADMIN_PASSWORD

# Use the provided startup scripts for automatic setup
./start-secure.sh    # Linux/macOS
.\start-secure.ps1   # Windows PowerShell
```

**Quick Start Issues**:
- Use provided startup scripts (`start-secure.ps1` or `start-secure.sh`) for automatic environment setup
- Scripts include password validation and helpful configuration output
- Scripts use standard port 8080 by default, with easy custom port options

### Support

For issues and questions:
1. Check existing [Issues](https://github.com/srinathSanjeeva/spring-rest-api/issues)
2. Create new issue with detailed description
3. Include logs and error messages
