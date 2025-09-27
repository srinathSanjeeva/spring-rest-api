package org.sanjeevas.springrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response DTO for API error handling
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@Schema(description = "Standard error response format")
public class ErrorResponseDto {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error code for categorization", example = "EMPLOYEE_NOT_FOUND")
    private String error;

    @Schema(description = "Brief error message", example = "Employee not found")
    private String message;

    @Schema(description = "Detailed error description", example = "Could not find employee with id: 123")
    private String details;

    @Schema(description = "Request path where error occurred", example = "/api/employees/123")
    private String path;

    @Schema(description = "Timestamp when error occurred")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    @Schema(description = "Validation errors (if applicable)")
    private List<FieldError> fieldErrors;

    @Schema(description = "Trace ID for debugging", example = "abc123-def456-ghi789")
    private String traceId;

    // Default constructor
    public ErrorResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for simple errors
    public ErrorResponseDto(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Full constructor
    public ErrorResponseDto(int status, String error, String message, String details, 
                           String path, List<FieldError> fieldErrors, String traceId) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
        this.path = path;
        this.fieldErrors = fieldErrors;
        this.traceId = traceId;
    }

    // Getters and Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<FieldError> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldError> fieldErrors) { this.fieldErrors = fieldErrors; }

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    /**
     * Field error details for validation errors
     */
    @Schema(description = "Field validation error details")
    public static class FieldError {
        @Schema(description = "Field name that failed validation", example = "name")
        private String field;

        @Schema(description = "Rejected value", example = "")
        private Object rejectedValue;

        @Schema(description = "Validation error message", example = "Name cannot be blank")
        private String message;

        public FieldError() {}

        public FieldError(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        // Getters and Setters
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public Object getRejectedValue() { return rejectedValue; }
        public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    @Override
    public String toString() {
        return "ErrorResponseDto{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
