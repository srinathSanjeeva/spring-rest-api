# PowerShell test runner script for Spring REST API
# Sets up secure test environment and runs tests

param(
    [switch]$SkipValidation,
    [switch]$Verbose
)

Write-Host "🧪 Setting up secure test environment..." -ForegroundColor Green

# Set test environment variables
$env:APP_SECURITY_ADMIN_PASSWORD = "TestAdmin123!"
$env:APP_SECURITY_USER_PASSWORD = "TestUser123!"
$env:APP_SECURITY_JWT_SECRET = "TestJWTSecretKeyForGitHubActionsCI123456789!"

Write-Host "✅ Test credentials configured" -ForegroundColor Green
Write-Host "📋 Configuration:" -ForegroundColor Cyan
Write-Host "   • Admin Username: testadmin" -ForegroundColor White
Write-Host "   • User Username: testuser" -ForegroundColor White
Write-Host "   • Password validation: ✅ Enabled" -ForegroundColor Green
Write-Host "   • JWT Secret: ✅ Set ($($env:APP_SECURITY_JWT_SECRET.Length) characters)" -ForegroundColor Green
Write-Host ""

Write-Host "🚀 Running tests..." -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Yellow

# Run tests with proper environment
if ($Verbose) {
    mvn test -X
} else {
    mvn test
}

# Check test results
if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ All tests passed!" -ForegroundColor Green
    Write-Host "📊 Test reports available in: target/surefire-reports/" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "❌ Some tests failed!" -ForegroundColor Red
    Write-Host "📊 Check test reports in: target/surefire-reports/" -ForegroundColor Cyan
    exit 1
}
