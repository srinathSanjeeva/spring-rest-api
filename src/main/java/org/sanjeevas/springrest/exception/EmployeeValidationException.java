package org.sanjeevas.springrest.exception;

/**
 * Custom exception for employee validation errors
 * 
 * @author Sanjeeva
 * @version 1.0
 */
public class EmployeeValidationException extends RuntimeException {
    
    private final String field;
    private final transient Object rejectedValue;

    public EmployeeValidationException(String message) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
    }

    public EmployeeValidationException(String field, Object rejectedValue, String message) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    public EmployeeValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
        this.rejectedValue = null;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
