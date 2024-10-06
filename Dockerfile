# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the Docker container
WORKDIR /app

# Copy the application JAR file (replace 'spring-rest.jar' with your actual JAR file)
COPY target/spring-rest.jar app.jar

# Expose port 8080 (default port for Spring Boot applications)
EXPOSE 8080

# Add an environment variable for active profile, default to H2 if none provided
ENV SPRING_PROFILES_ACTIVE h2

# Run the Spring Boot application with the specified profile
ENTRYPOINT ["java", "-jar", "app.jar"]