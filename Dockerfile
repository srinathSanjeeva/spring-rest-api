# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the Docker container
WORKDIR /app

# Copy the generated WAR file
COPY target/spring-rest*.war app.war

# Expose port 8080 (default port for Spring Boot applications)
EXPOSE 8080

# Add an environment variable for active profile, default to H2 if none provided
ENV SPRING_PROFILES_ACTIVE ${SPRING_PROFILES_ACTIVE:-h2}

# Add environment variables for the database connection
ENV SPRING_DATASOURCE_URL=ENV SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQL_DB_HOSTNAME:-localhost}:3306/${MYSQL_DATABASE:-mydb}

# Pass environment variables (DB_USER, DB_PASSWORD) to the application
ENV SPRING_DATASOURCE_USERNAME=${DB_USER}
ENV SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

# Run the Spring Boot application with the specified profile
ENTRYPOINT ["java", "-jar", "app.war"]
