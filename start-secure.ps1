# PowerShell script to start the Spring REST API with secure environment variables
# Usage: .\start-secure.ps1 [port]

param(
    [int]$Port = 8080,
    [string]$AdminPassword = "SecureAdmin123!",
    [string]$UserPassword = "SecureUser123!",
    [string]$JwtSecret = "MyVerySecureJWTSecretKeyThatIsLongEnoughForProduction123!"
)

Write-Host "🚀 Starting Employee Management REST API with Enhanced Security" -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Yellow

# Validate password requirements
function Test-PasswordStrength {
    param([string]$Password)
    
    if ($Password.Length -lt 8) {
        return $false, "Password must be at least 8 characters long"
    }
    
    if (-not ($Password -cmatch '[a-z]')) {
        return $false, "Password must contain at least one lowercase letter"
    }
    
    if (-not ($Password -cmatch '[A-Z]')) {
        return $false, "Password must contain at least one uppercase letter"
    }
    
    if (-not ($Password -cmatch '\d')) {
        return $false, "Password must contain at least one digit"
    }
    
    if (-not ($Password -cmatch '[@$!%*?&]')) {
        return $false, "Password must contain at least one special character (@$!%*?&)"
    }
    
    return $true, "Password meets strength requirements"
}

# Validate passwords
$adminValid, $adminMessage = Test-PasswordStrength $AdminPassword
$userValid, $userMessage = Test-PasswordStrength $UserPassword

if (-not $adminValid) {
    Write-Host "❌ Admin password validation failed: $adminMessage" -ForegroundColor Red
    exit 1
}

if (-not $userValid) {
    Write-Host "❌ User password validation failed: $userMessage" -ForegroundColor Red
    exit 1
}

if ($JwtSecret.Length -lt 32) {
    Write-Host "❌ JWT secret must be at least 32 characters long" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Password validation passed" -ForegroundColor Green

# Set environment variables
$env:APP_SECURITY_ADMIN_PASSWORD = $AdminPassword
$env:APP_SECURITY_USER_PASSWORD = $UserPassword
$env:APP_SECURITY_JWT_SECRET = $JwtSecret
$env:SERVER_PORT = $Port.ToString()

Write-Host "📋 Configuration:" -ForegroundColor Cyan
Write-Host "   • Server Port: $Port" -ForegroundColor White
Write-Host "   • Admin Username: admin" -ForegroundColor White
Write-Host "   • User Username: user" -ForegroundColor White
Write-Host "   • Password validation: ✅ Passed" -ForegroundColor Green
Write-Host "   • JWT Secret: ✅ Set (${($JwtSecret.Length)} characters)" -ForegroundColor Green
Write-Host ""

Write-Host "🔗 Quick Access URLs:" -ForegroundColor Cyan
Write-Host "   • API Base: http://localhost:$Port/api/v1/employees" -ForegroundColor White
Write-Host "   • Swagger UI: http://localhost:$Port/swagger-ui.html" -ForegroundColor White
Write-Host "   • Health Check: http://localhost:$Port/actuator/health" -ForegroundColor White
Write-Host "   • H2 Console: http://localhost:$Port/h2-console" -ForegroundColor White
Write-Host ""

Write-Host "🧪 Test Commands:" -ForegroundColor Cyan
Write-Host "   curl -u admin:$AdminPassword http://localhost:$Port/api/v1/employees" -ForegroundColor White
Write-Host "   curl -u user:$UserPassword http://localhost:$Port/api/v1/employees" -ForegroundColor White
Write-Host ""

Write-Host "🚀 Starting application..." -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Yellow

# Start the application
try {
    .\mvnw.cmd spring-boot:run
} catch {
    Write-Host "❌ Failed to start application: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
