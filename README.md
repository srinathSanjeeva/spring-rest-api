### `README.md`

# Spring Boot REST API with H2 and MySQL Database

This project is a Spring Boot-based REST API that can connect to either an H2 in-memory database or a MySQL database, depending on the environment. The database configuration is managed using Spring profiles.

## Features

- REST API built using Spring Boot.
- Supports both H2 (in-memory) and MySQL databases.
- Docker and Docker Compose setup for easy deployment.
- Automatic database schema generation and initialization.

## Prerequisites

- Java 17+
- Maven 3+
- Docker (optional, for containerization)
- MySQL (optional, for MySQL database)

## Project Structure

- **Spring Boot REST API**: Implements CRUD operations.
- **Database Profiles**: Switch between H2 (for development and testing) and MySQL (for production).
- **Docker Integration**: Containerize the application and run it with either database.

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/srinathSanjeeva/spring-rest.git
cd spring-rest
```

````

### 2. Build the Project

You can build the project using Maven.

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

### 4. Docker Instructions

#### Build Docker Image

You can build the Docker image of the application using the following command:

```bash
docker build -t spring-rest-app .
```

#### Run Docker Container

You can run the Docker container with either the H2 or MySQL profile.

- **To run with H2 (default):**

```bash
docker run -d -p 8080:8080 spring-rest-app
```

- **To run with MySQL:**

```bash
docker run -d -p 8080:8080 -e SPRING_PROFILES_ACTIVE=mysql spring-rest-app
```

### 5. Using Docker Compose

You can orchestrate the application with a MySQL database using Docker Compose.

#### Step 1: Modify `docker-compose.yml`

Ensure that the `docker-compose.yml` file is configured for your MySQL database credentials.

#### Step 2: Run with Docker Compose

Run the application with Docker Compose:

```bash
docker-compose up
```

### 6. API Endpoints

The following REST API endpoints are exposed by the application:

- **GET** `/api/users`: Get all users.
- **GET** `/api/users/{id}`: Get user by ID.
- **POST** `/api/users`: Create a new user.
- **PUT** `/api/users/{id}`: Update user by ID.
- **DELETE** `/api/users/{id}`: Delete user by ID.

### 7. Environment Variables

The following environment variables are used to configure the application:

- `SPRING_DATASOURCE_URL`: JDBC URL for the database (e.g., MySQL or H2).
- `SPRING_DATASOURCE_USERNAME`: Username for the database.
- `SPRING_DATASOURCE_PASSWORD`: Password for the database.
- `SPRING_PROFILES_ACTIVE`: Active Spring profile (`h2` or `mysql`).

### 8. Switching Between H2 and MySQL

The default profile is set to `h2` (in-memory database). To switch to MySQL, use the `SPRING_PROFILES_ACTIVE` environment variable:

```bash
docker run -d -p 8080:8080 -e SPRING_PROFILES_ACTIVE=mysql spring-rest-app
```

### 9. License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

### Summary

This project provides a Spring Boot REST API that supports both H2 and MySQL databases, with the ability to switch between them using Spring profiles. The project can be easily containerized and deployed using Docker and Docker Compose.

```

### How to Use this `README.md`:
1. Place this content in the root of your project directory in a file called `README.md`.
2. Replace the repository URL and any other specific details where needed.
3. This markdown file contains instructions on how to run the application both locally and with Docker, and explains how to switch between H2 and MySQL databases using Spring profiles.

Let me know if you need further adjustments or additions!
```
````
