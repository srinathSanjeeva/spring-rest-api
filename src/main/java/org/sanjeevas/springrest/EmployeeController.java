package org.sanjeevas.springrest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.sanjeevas.springrest.dto.EmployeeDto;
import org.sanjeevas.springrest.dto.EmployeeListResponseDto;
import org.sanjeevas.springrest.dto.ErrorResponseDto;
import org.sanjeevas.springrest.exception.EmployeeNotFoundException;
import org.sanjeevas.springrest.security.SecurityUtils;
import org.sanjeevas.springrest.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST Controller for Employee management operations
 * Implements enterprise-level best practices including validation, 
 * documentation, security, logging, and proper error handling
 * 
 * @author Sanjeeva
 * @version 2.0
 */
@RestController
@RequestMapping("/api/v1/employees")
@Validated
@Tag(name = "Employee Management", description = "API for managing employees")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Get all employees", 
               description = "Retrieve a list of all employees with optional pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employees",
                    content = @Content(schema = @Schema(implementation = EmployeeListResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeListResponseDto> getAllEmployees(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) int size,
            
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Getting all employees - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);

        Sort.Direction direction = SecurityUtils.normalizeUnicode(sortDir).equalsIgnoreCase("desc") ? 
                                  Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<EmployeeDto> employeePage = employeeService.findAll(pageable);
        EmployeeListResponseDto response = new EmployeeListResponseDto(
            employeePage.getContent(), 
            page, 
            size, 
            employeePage.getTotalElements()
        );

        logger.info("Successfully retrieved {} employees", employeePage.getNumberOfElements());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get employee by ID", 
               description = "Retrieve a specific employee by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee found",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDto> getEmployeeById(
            @Parameter(description = "Employee ID", required = true, example = "1")
            @PathVariable @Min(1) Long id) {
        
        logger.info("Getting employee with id: {}", id);
        
        return employeeService.findById(id)
                .map(employee -> {
                    logger.info("Employee found with id: {}", id);
                    return ResponseEntity.ok(employee);
                })
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException(id);
                });
    }

    @Operation(summary = "Create new employee", 
               description = "Create a new employee with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "Employee already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDto> createEmployee(
            @Parameter(description = "Employee data", required = true)
            @Valid @RequestBody EmployeeDto employeeDto) {
        
        logger.info("Creating new employee: {}", employeeDto);
        
        EmployeeDto createdEmployee = employeeService.create(employeeDto);
        URI location = URI.create(String.format("/api/v1/employees/%d", createdEmployee.getId()));
        
        logger.info("Employee created successfully with id: {}", createdEmployee.getId());
        return ResponseEntity.created(location).body(createdEmployee);
    }

    @Operation(summary = "Update employee", 
               description = "Update an existing employee or create new one if not exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @Parameter(description = "Employee ID", required = true, example = "1")
            @PathVariable @Min(1) Long id,
            
            @Parameter(description = "Updated employee data", required = true)
            @Valid @RequestBody EmployeeDto employeeDto) {
        
        logger.info("Updating employee with id: {}, data: {}", id, employeeDto);
        
        boolean exists = employeeService.existsById(id);
        EmployeeDto updatedEmployee = employeeService.update(id, employeeDto);
        
        if (exists) {
            logger.info("Employee updated successfully with id: {}", id);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            logger.info("Employee created successfully with id: {}", id);
            URI location = URI.create(String.format("/api/v1/employees/%d", id));
            return ResponseEntity.created(location).body(updatedEmployee);
        }
    }

    @Operation(summary = "Partially update employee", 
               description = "Partially update an existing employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
        @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDto> partialUpdateEmployee(
            @Parameter(description = "Employee ID", required = true, example = "1")
            @PathVariable @Min(1) Long id,
            
            @Parameter(description = "Partial employee data", required = true)
            @RequestBody EmployeeDto employeeDto) {
        
        logger.info("Partially updating employee with id: {}, data: {}", id, employeeDto);
        
        EmployeeDto updatedEmployee = employeeService.partialUpdate(id, employeeDto);
        if (updatedEmployee != null) {
            logger.info("Employee partially updated successfully with id: {}", id);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            logger.warn("Employee not found for partial update with id: {}", id);
            throw new EmployeeNotFoundException(id);
        }
    }

    @Operation(summary = "Delete employee", 
               description = "Delete an employee by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID", required = true, example = "1")
            @PathVariable @Min(1) Long id) {
        
        logger.info("Deleting employee with id: {}", id);
        
        boolean deleted = employeeService.deleteById(id);
        if (deleted) {
            logger.info("Employee deleted successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Employee not found for deletion with id: {}", id);
            throw new EmployeeNotFoundException(id);
        }
    }

    @Operation(summary = "Search employees by name", 
               description = "Search for employees by name (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeListResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeListResponseDto> searchEmployeesByName(
            @Parameter(description = "Name to search for", required = true, example = "John")
            @RequestParam String name) {
        
        logger.info("Searching employees by name: {}", name);
        
        List<EmployeeDto> employees = employeeService.findByNameContainingIgnoreCase(name);
        EmployeeListResponseDto response = new EmployeeListResponseDto(employees);
        
        logger.info("Found {} employees matching name: {}", employees.size(), name);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get employees by role", 
               description = "Retrieve all employees with a specific role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeListResponseDto.class)))
    })
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeListResponseDto> getEmployeesByRole(
            @Parameter(description = "Employee role", required = true, example = "Developer")
            @PathVariable String role) {
        
        logger.info("Getting employees by role: {}", role);
        
        List<EmployeeDto> employees = employeeService.findByRole(role);
        EmployeeListResponseDto response = new EmployeeListResponseDto(employees);
        
        logger.info("Found {} employees with role: {}", employees.size(), role);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get employee count", 
               description = "Get the total number of employees")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    @GetMapping("/count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> getEmployeeCount() {
        logger.info("Getting total employee count");
        
        long count = employeeService.count();
        
        logger.info("Total employee count: {}", count);
        return ResponseEntity.ok(count);
    }
}
