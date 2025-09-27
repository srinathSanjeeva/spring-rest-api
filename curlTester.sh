#!/usr/bin/env bash
echo "Testing this spring boot application with some curl commands"
echo "Using Basic Authentication with user:password"
echo ""

echo "=== Getting all employees ==="
curl -u user:password -v localhost:8080/employees

echo ""
echo "=== Testing for a specific employee ==="
curl -u user:password -v localhost:8080/employees/1

echo ""
echo "=== Test for non-existent employee ==="
curl -u user:password -v localhost:8080/employees/99

echo ""
echo "=== Adding a new employee ==="
curl -X POST -u user:password localhost:8080/employees \
  -H 'Content-type:application/json' \
  -d '{"name": "Samwise Gamgee", "role": "gardener"}'

echo ""
echo "=== Let us update the employee ==="
curl -X PUT -u user:password localhost:8080/employees/3 \
  -H 'Content-type:application/json' \
  -d '{"name": "Samwise Gamgee", "role": "ring bearer"}'

echo ""
echo "=== Now let us delete the employee ==="
curl -X DELETE -u user:password localhost:8080/employees/3

echo ""
echo "=== Testing actuator endpoints ==="
curl -u user:password localhost:8080/actuator

echo ""
echo "Testing complete!"