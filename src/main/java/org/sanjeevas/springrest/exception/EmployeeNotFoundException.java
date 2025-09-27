package org.sanjeevas.springrest.exception;

/**
 * Custom exception for when an employee is not found
 * 
 * @author Sanjeeva
 * @version 1.0
 */
public class EmployeeNotFoundException extends RuntimeException {
    
    private final Long employeeId;

    public EmployeeNotFoundException(Long id) {
        super("Could not find employee with id: " + id);
        this.employeeId = id;
    }

    public EmployeeNotFoundException(String message) {
        super(message);
        this.employeeId = null;
    }

    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.employeeId = null;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
