#!/bin/bash

# Test runner script for Spring REST API
# Sets up secure test environment and runs tests

echo "🧪 Setting up secure test environment..."

# Set test environment variables
export APP_SECURITY_ADMIN_PASSWORD="TestAdmin123!"
export APP_SECURITY_USER_PASSWORD="TestUser123!"
export APP_SECURITY_JWT_SECRET="TestJWTSecretKeyForGitHubActionsCI123456789!"

echo "✅ Test credentials configured"
echo "📋 Configuration:"
echo "   • Admin Username: testadmin"
echo "   • User Username: testuser"
echo "   • Password validation: ✅ Enabled"
echo "   • JWT Secret: ✅ Set (${#APP_SECURITY_JWT_SECRET} characters)"
echo ""

echo "🚀 Running tests..."
echo "=================================="

# Run tests with proper environment
mvn test

# Check test results
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ All tests passed!"
    echo "📊 Test reports available in: target/surefire-reports/"
else
    echo ""
    echo "❌ Some tests failed!"
    echo "📊 Check test reports in: target/surefire-reports/"
    exit 1
fi
