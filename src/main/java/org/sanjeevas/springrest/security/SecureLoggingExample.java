package org.sanjeevas.springrest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Example demonstrating secure logging practices with standard SLF4J and Logback
 * This replaces the custom SecureLogger with industry-standard approaches
 * 
 * @author Security Team
 * @version 2.0
 */
@Component
public class SecureLoggingExample {
    
    private static final Logger logger = LoggerFactory.getLogger(SecureLoggingExample.class);
    
    /**
     * Example of secure logging for authentication events
     * Uses security marker for audit trail
     */
    public void logAuthenticationSuccess(String username, String remoteAddr) {
        // Logback automatically handles CRLF injection via configuration
        // The %replace pattern in logback-spring.xml sanitizes the output
        logger.info(SecurityUtils.SECURITY_MARKER, 
                   "Authentication successful - User: {}, IP: {}", 
                   username, remoteAddr);
    }
    
    /**
     * Example of secure logging for authorization failures
     */
    public void logAuthorizationFailure(String username, String resource) {
        logger.warn(SecurityUtils.SECURITY_MARKER,
                   "Authorization failed - User: {} attempted access to: {}", 
                   username, resource);
    }
    
    /**
     * Example of secure logging for data access
     */
    public void logDataAccess(String operation, String entityType, Long entityId, String username) {
        logger.info(SecurityUtils.SECURITY_MARKER,
                   "Data access - Operation: {}, Entity: {} ID: {}, User: {}", 
                   operation, entityType, entityId, username);
    }
    
    /**
     * Example of secure logging for input validation failures
     */
    public void logValidationFailure(String fieldName, String invalidValue, String errorMessage) {
        // Note: Logback configuration will automatically sanitize invalidValue
        logger.warn("Input validation failed - Field: {}, Value: {}, Error: {}", 
                   fieldName, invalidValue, errorMessage);
    }
    
    /**
     * Example of secure logging for system errors
     */
    public void logSystemError(String operation, String errorDetails, Exception exception) {
        logger.error("System error during {} - Details: {}", 
                    operation, errorDetails, exception);
    }
}
