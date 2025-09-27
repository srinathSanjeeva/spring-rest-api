package org.sanjeevas.springrest.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Security configuration properties with validation
 * Prevents hardcoded credentials and enforces strong passwords
 * 
 * @author Security Team
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "app.security")
@Validated
public class SecurityProperties {
    
    /**
     * Admin user configuration
     */
    private Admin admin = new Admin();
    
    /**
     * Regular user configuration  
     */
    private User user = new User();
    
    /**
     * CORS allowed origins
     */
    private String[] allowedOrigins = {
        "http://localhost:3000", 
        "http://localhost:8080", 
        "http://localhost:8081"
    };
    
    /**
     * JWT configuration
     */
    private Jwt jwt = new Jwt();
    
    public Admin getAdmin() {
        return admin;
    }
    
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }
    
    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
    
    public Jwt getJwt() {
        return jwt;
    }
    
    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }
    
    public static class Admin {
        
        @NotBlank(message = "Admin username cannot be blank")
        @Size(min = 3, max = 20, message = "Admin username must be between 3 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Admin username contains invalid characters")
        private String username = "admin";
        
        @NotBlank(message = "Admin password cannot be blank")
        @Size(min = 8, max = 100, message = "Admin password must be between 8 and 100 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", 
                message = "Admin password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    public static class User {
        
        @NotBlank(message = "User username cannot be blank")
        @Size(min = 3, max = 20, message = "User username must be between 3 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "User username contains invalid characters")
        private String username = "user";
        
        @NotBlank(message = "User password cannot be blank")
        @Size(min = 8, max = 100, message = "User password must be between 8 and 100 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", 
                message = "User password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    public static class Jwt {
        
        @NotBlank(message = "JWT secret cannot be blank")
        @Size(min = 32, message = "JWT secret must be at least 32 characters")
        private String secret;
        
        private long expirationMs = 86400000; // 24 hours
        
        public String getSecret() {
            return secret;
        }
        
        public void setSecret(String secret) {
            this.secret = secret;
        }
        
        public long getExpirationMs() {
            return expirationMs;
        }
        
        public void setExpirationMs(long expirationMs) {
            this.expirationMs = expirationMs;
        }
    }
}
