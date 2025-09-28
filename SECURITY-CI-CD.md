# Security Configuration for CI/CD

This document explains how to securely configure the Spring REST API project for Continuous Integration and Deployment while maintaining security best practices.

## 🔒 Security Approach

### Local Development
- Use the provided `start-secure.ps1` or `start-secure.sh` scripts
- These scripts validate password strength and set environment variables
- Never commit actual passwords to the repository

### CI/CD (GitHub Actions)
- Uses test-specific passwords that are safe for CI environment
- Passwords are generated dynamically in the CI pipeline
- No sensitive production credentials are exposed

## 📋 Required GitHub Secrets

For full CI/CD functionality, add these secrets to your GitHub repository:

### Docker Deployment (Optional)
- `DOCKER_USERNAME`: Your Docker Hub username
- `DOCKER_PASSWORD`: Your Docker Hub password or access token

### Security Scanning
- `NVD_API_KEY`: (Optional) National Vulnerability Database API key for faster dependency checks

## 🚀 GitHub Actions Workflow

The CI/CD pipeline includes three main jobs:

### 1. Test Job
```yaml
env:
  APP_SECURITY_ADMIN_PASSWORD: TestAdmin123!
  APP_SECURITY_USER_PASSWORD: TestUser123!
  APP_SECURITY_JWT_SECRET: TestJWTSecretKeyForGitHubActionsCI123456789!
```

### 2. Security Scan Job
- OWASP Dependency Check for vulnerability scanning
- SpotBugs with FindSecBugs for static security analysis
- Runs only on main branch pushes

### 3. Docker Build Job (Tag Releases Only)
- Builds and pushes Docker images for tagged releases
- Supports multi-platform builds (AMD64, ARM64)
- Uses Docker Hub secrets for authentication

## 🛡️ Security Best Practices Implemented

### 1. **No Hardcoded Credentials**
- All passwords provided via environment variables
- Validation ensures strong password requirements
- Separate test credentials for CI/CD

### 2. **Environment Isolation**
- Test environment uses dedicated configuration
- CI/CD credentials are different from production
- Each job uses purpose-specific passwords

### 3. **Secure Defaults**
- Test configuration disables unnecessary features
- Logging levels reduced in test environment
- H2 console disabled in tests

### 4. **Vulnerability Scanning**
- Automated dependency vulnerability checks
- Static code analysis for security issues
- Reports uploaded as artifacts for review

## 📝 Test Configuration

The `application-test.properties` file provides:
- Secure defaults for test environment
- Environment variable fallbacks
- Minimal logging and features for faster tests

## 🔧 Local Testing

To run tests locally with proper security:

### PowerShell
```powershell
$env:APP_SECURITY_ADMIN_PASSWORD="TestAdmin123!"
$env:APP_SECURITY_USER_PASSWORD="TestUser123!"
$env:APP_SECURITY_JWT_SECRET="TestJWTSecretKeyForGitHubActionsCI123456789!"
mvn test
```

### Bash
```bash
export APP_SECURITY_ADMIN_PASSWORD="TestAdmin123!"
export APP_SECURITY_USER_PASSWORD="TestUser123!"
export APP_SECURITY_JWT_SECRET="TestJWTSecretKeyForGitHubActionsCI123456789!"
mvn test
```

## 🚨 Security Considerations

### ✅ Safe for Public Repository
- No production credentials in code or CI
- Test passwords are clearly marked as test-only
- All sensitive data provided via environment variables

### ✅ Strong Password Validation
- Minimum 8 characters
- Requires uppercase, lowercase, numbers, special chars
- Validated both in application and startup scripts

### ✅ Comprehensive Security Scanning
- OWASP Dependency Check for known vulnerabilities
- SpotBugs with security-focused rules
- Automated on every main branch push

## 📊 Monitoring and Reporting

The CI/CD pipeline generates:
- Test reports (JUnit format)
- Security scan reports (HTML/XML)
- Build artifacts (JAR files)
- Docker images (for releases)

All reports are uploaded as GitHub Actions artifacts for review.

## 🔄 Updating Security Configuration

To modify security settings:

1. Update `SecurityProperties.java` for validation rules
2. Modify `application-test.properties` for test defaults
3. Update GitHub Actions workflow for CI environment variables
4. Test locally before pushing changes

## 📚 References

- [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/)
- [SpotBugs Security Rules](https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html)
- [Spring Security Best Practices](https://docs.spring.io/spring-security/reference/features/exploits/index.html)
- [GitHub Actions Security](https://docs.github.com/en/actions/security-guides/security-hardening-for-github-actions)
