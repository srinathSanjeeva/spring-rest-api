#!/bin/bash
mvn clean package -DskipTests
# Run docker-compose up
docker-compose up
