#!/bin/bash

# Bash script to start the Spring REST API with secure environment variables
# Usage: ./start-secure.sh [port] [admin_password] [user_password] [jwt_secret]

PORT=${1:-8080}
ADMIN_PASSWORD=${2:-"SecureAdmin123!"}
USER_PASSWORD=${3:-"SecureUser123!"}
JWT_SECRET=${4:-"MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"}

echo "🚀 Starting Employee Management REST API with Enhanced Security"
echo "====================================================================="

# Function to validate password strength
validate_password() {
    local password="$1"
    local name="$2"
    
    if [[ ${#password} -lt 8 ]]; then
        echo "❌ $name password must be at least 8 characters long"
        return 1
    fi
    
    if [[ ! "$password" =~ [a-z] ]]; then
        echo "❌ $name password must contain at least one lowercase letter"
        return 1
    fi
    
    if [[ ! "$password" =~ [A-Z] ]]; then
        echo "❌ $name password must contain at least one uppercase letter"
        return 1
    fi
    
    if [[ ! "$password" =~ [0-9] ]]; then
        echo "❌ $name password must contain at least one digit"
        return 1
    fi
    
    if [[ ! "$password" =~ [@\$!%\*\?\&] ]]; then
        echo "❌ $name password must contain at least one special character (@\$!%*?&)"
        return 1
    fi
    
    return 0
}

# Validate passwords
validate_password "$ADMIN_PASSWORD" "Admin" || exit 1
validate_password "$USER_PASSWORD" "User" || exit 1

if [[ ${#JWT_SECRET} -lt 32 ]]; then
    echo "❌ JWT secret must be at least 32 characters long"
    exit 1
fi

echo "✅ Password validation passed"

# Set environment variables
export APP_SECURITY_ADMIN_PASSWORD="$ADMIN_PASSWORD"
export APP_SECURITY_USER_PASSWORD="$USER_PASSWORD"
export APP_SECURITY_JWT_SECRET="$JWT_SECRET"
export SERVER_PORT="$PORT"

echo "📋 Configuration:"
echo "   • Server Port: $PORT"
echo "   • Admin Username: admin"
echo "   • User Username: user"
echo "   • Password validation: ✅ Passed"
echo "   • JWT Secret: ✅ Set (${#JWT_SECRET} characters)"
echo ""

echo "🔗 Quick Access URLs:"
echo "   • API Base: http://localhost:$PORT/api/v1/employees"
echo "   • Swagger UI: http://localhost:$PORT/swagger-ui.html"
echo "   • Health Check: http://localhost:$PORT/actuator/health"
echo "   • H2 Console: http://localhost:$PORT/h2-console"
echo ""

echo "🧪 Test Commands:"
echo "   curl -u admin:$ADMIN_PASSWORD http://localhost:$PORT/api/v1/employees"
echo "   curl -u user:$USER_PASSWORD http://localhost:$PORT/api/v1/employees"
echo ""

echo "🚀 Starting application..."
echo "====================================================================="

# Start the application
if [[ -f "./mvnw" ]]; then
    ./mvnw spring-boot:run
else
    echo "❌ Maven wrapper not found. Please run from project root directory."
    exit 1
fi
