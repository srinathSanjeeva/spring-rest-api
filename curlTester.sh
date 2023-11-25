#!/usr/bin/env bash
echo "Testing this spring boot application with some curl commands"

curl -v localhost:8080/employees

echo "Now testing for a specific employee"
curl -v localhost:8080/employees/1

echo "Now test for non-existent employee"
curl -v localhost:8080/employees/99

echo "Adding a new employee"
curl -X POST localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "gardener"}'

echo " //n Let us update the employee"
curl -X PUT localhost:8080/employees/8 -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "ring bearer"}'

echo "//n Now let us delete the employee"
curl -X DELETE localhost:8080/employees/8