# Spring Boot REST API with H2 and MySQL Database

This project is a Spring Boot-based REST API that can connect to either an H2 in-memory database or a MySQL database, depending on the environment. The database configuration is managed using Spring profiles.

[![Test Coverage](https://img.shields.io/badge/coverage-0%25-red)](https://img.shields.io/badge/coverage-0%25-red)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/srinathSanjeeva/spring-rest-api.git
cd spring-rest-api
```

### 2. Build the Project

```bash
mvn clean package
```

### 3. Run Locally

You can run the application with either an H2 or MySQL database by specifying the active profile.

#### Running with H2 (Default)

To run with H2 (in-memory database), use the following command:

```bash
mvn spring-boot:run
```

#### Running with MySQL

To run the application with MySQL, first ensure you have a running MySQL instance, and configure your `application-mysql.properties` with the correct database credentials.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### 4. API Contract and Swagger UI

The API contract is defined in the `api.json` file, which is an OpenAPI 3.0 specification.

When running the application with the `dev` profile, you can access the Swagger UI to interact with the API at the following URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

To run with the `dev` profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 5. Docker Instructions

You can build and run the application using Docker.

#### Build Docker Image

```bash
docker build -t spring-rest-app .
```

#### Run Docker Container

- **To run with H2 (default):**

```bash
docker run -d -p 8080:8080 spring-rest-app
```

- **To run with MySQL:**

```bash
docker run -d -p 8080:8080 -e SPRING_PROFILES_ACTIVE=mysql spring-rest-app
```

- **To run with Swagger UI enabled (dev profile):**

```bash
docker run -d -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev spring-rest-app
```
