package org.sanjeevas.springrest.security;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Security utility class for input sanitization and validation
 * Works with Logback's built-in CRLF protection
 * 
 * @author Security Team
 * @version 2.0
 */
public class SecurityUtils {
    
    // Security marker for audit logging
    public static final Marker SECURITY_MARKER = MarkerFactory.getMarker("SECURITY");
    
    // Private constructor to prevent instantiation
    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Pattern to detect potentially malicious characters
    private static final Pattern MALICIOUS_PATTERN = Pattern.compile("[\\x00-\\x1F\\x7F-\\x9F]");
    
    /**
     * Normalizes Unicode strings to prevent Unicode-based attacks
     * Note: CRLF protection is now handled by Logback configuration
     * 
     * @param input The input string to normalize
     * @return Normalized string
     */
    public static String normalizeUnicode(String input) {
        if (input == null) {
            return null;
        }
        
        // Normalize to NFC (Canonical Decomposition followed by Canonical Composition)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFC);
        
        // Additional security: remove any remaining control characters
        return MALICIOUS_PATTERN.matcher(normalized).replaceAll("");
    }
    
    /**
     * Validates and sanitizes employee name input
     * 
     * @param name The employee name to validate
     * @return Sanitized name
     * @throws IllegalArgumentException if name is invalid
     */
    public static String validateAndSanitizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        String sanitized = normalizeUnicode(name.trim());
        if (sanitized == null) {
            throw new IllegalArgumentException("Name normalization failed");
        }
        
        // Check for reasonable length
        if (sanitized.length() > 100) {
            throw new IllegalArgumentException("Name is too long (max 100 characters)");
        }
        
        // Basic pattern validation - allow letters, spaces, hyphens, apostrophes
        if (!sanitized.matches("^[\\p{L}\\p{M}\\s'.-]+$")) {
            throw new IllegalArgumentException("Name contains invalid characters");
        }
        
        return sanitized;
    }
    
    /**
     * Validates and sanitizes employee role input
     * 
     * @param role The employee role to validate
     * @return Sanitized name
     * @throws IllegalArgumentException if role is invalid
     */
    public static String validateAndSanitizeRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        
        String sanitized = normalizeUnicode(role.trim());
        if (sanitized == null) {
            throw new IllegalArgumentException("Role normalization failed");
        }
        
        // Check for reasonable length
        if (sanitized.length() > 50) {
            throw new IllegalArgumentException("Role is too long (max 50 characters)");
        }
        
        // Allow letters, numbers, spaces, and some special characters
        if (!sanitized.matches("^[\\p{L}\\p{M}\\p{N}\\s._-]+$")) {
            throw new IllegalArgumentException("Role contains invalid characters");
        }
        
        return sanitized;
    }
    
    /**
     * Validates pagination parameters
     * 
     * @param page Page number
     * @param size Page size
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        
        if (size > 1000) {
            throw new IllegalArgumentException("Page size too large (max 1000)");
        }
    }
    
    /**
     * Validates sort parameter to prevent injection attacks
     * 
     * @param sortBy The sort field name
     * @throws IllegalArgumentException if sort field is invalid
     */
    public static void validateSortField(String sortBy) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return; // Allow null/empty for default sorting
        }
        
        // Only allow alphanumeric characters and underscores (typical field names)
        if (!sortBy.matches("^[a-zA-Z_]\\w*$")) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        
        // Whitelist allowed sort fields
        String[] allowedFields = {"id", "name", "role", "createdAt", "updatedAt"};
        for (String allowedField : allowedFields) {
            if (allowedField.equals(sortBy)) {
                return; // Field is allowed
            }
        }
        
        throw new IllegalArgumentException("Sort field not allowed: " + sortBy);
    }
}
