package org.sanjeevas.springrest.exception;

import org.sanjeevas.springrest.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for the Employee REST API
 * Provides consistent error responses across the application
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle EmployeeNotFoundException
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEmployeeNotFoundException(
            EmployeeNotFoundException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Employee not found - TraceId: {}, Message: {}", traceId, ex.getMessage());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "EMPLOYEE_NOT_FOUND",
                "Employee not found",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle EmployeeValidationException
     */
    @ExceptionHandler(EmployeeValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleEmployeeValidationException(
            EmployeeValidationException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Employee validation error - TraceId: {}, Message: {}", traceId, ex.getMessage());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Validation failed",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle method argument validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Validation error - TraceId: {}, Errors: {}", traceId, ex.getBindingResult().getAllErrors().size());

        List<ErrorResponseDto.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDto.FieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Request validation failed",
                "One or more fields have validation errors",
                request.getRequestURI(),
                fieldErrors,
                traceId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle bind exceptions
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(
            BindException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Bind error - TraceId: {}, Message: {}", traceId, ex.getMessage());

        List<ErrorResponseDto.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDto.FieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "BINDING_ERROR",
                "Data binding failed",
                ex.getMessage(),
                request.getRequestURI(),
                fieldErrors,
                traceId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle constraint violation exceptions
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Constraint violation - TraceId: {}, Message: {}", traceId, ex.getMessage());

        List<ErrorResponseDto.FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ErrorResponseDto.FieldError(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()
                ))
                .toList();

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "CONSTRAINT_VIOLATION",
                "Constraint validation failed",
                ex.getMessage(),
                request.getRequestURI(),
                fieldErrors,
                traceId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle HTTP method not supported
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Method not supported - TraceId: {}, Method: {}", traceId, request.getMethod());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "METHOD_NOT_ALLOWED",
                "HTTP method not supported",
                String.format("Method '%s' is not supported for this endpoint. Supported methods: %s", 
                             ex.getMethod(), ex.getSupportedHttpMethods()),
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * Handle HTTP media type not supported
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Media type not supported - TraceId: {}, ContentType: {}", traceId, ex.getContentType());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "UNSUPPORTED_MEDIA_TYPE",
                "Media type not supported",
                String.format("Media type '%s' is not supported. Supported types: %s", 
                             ex.getContentType(), ex.getSupportedMediaTypes()),
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    /**
     * Handle missing request parameters
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Missing request parameter - TraceId: {}, Parameter: {}", traceId, ex.getParameterName());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "MISSING_PARAMETER",
                "Required parameter missing",
                String.format("Required parameter '%s' is missing", ex.getParameterName()),
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle method argument type mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Type mismatch - TraceId: {}, Parameter: {}, Value: {}", 
                   traceId, ex.getName(), ex.getValue());

        Class<?> requiredTypeClass = ex.getRequiredType();
        String requiredType = requiredTypeClass != null ? requiredTypeClass.getSimpleName() : "Unknown";
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "TYPE_MISMATCH",
                "Parameter type mismatch",
                String.format("Parameter '%s' with value '%s' could not be converted to type '%s'", 
                             ex.getName(), ex.getValue(), requiredType),
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle message not readable (JSON parsing errors)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("Message not readable - TraceId: {}, Message: {}", traceId, ex.getMessage());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "MALFORMED_JSON",
                "Request body is not readable",
                "The request body contains malformed JSON or is not readable",
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle no handler found (404 errors)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.warn("No handler found - TraceId: {}, URL: {}", traceId, ex.getRequestURL());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "ENDPOINT_NOT_FOUND",
                "Endpoint not found",
                String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL()),
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        String traceId = generateTraceId();
        logger.error("Unexpected error - TraceId: {}, Message: {}", traceId, ex.getMessage(), ex);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                "Please contact support with the trace ID",
                request.getRequestURI(),
                null,
                traceId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Generate a unique trace ID for error tracking
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
