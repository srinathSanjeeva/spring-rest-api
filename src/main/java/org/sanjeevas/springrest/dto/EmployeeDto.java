package org.sanjeevas.springrest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for Employee
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@Schema(description = "Employee Data Transfer Object")
public class EmployeeDto {

    @Schema(description = "Employee ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters")
    @Schema(description = "Employee full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Employee role is required")
    @Size(min = 2, max = 50, message = "Employee role must be between 2 and 50 characters")
    @Schema(description = "Employee role/position", example = "Software Engineer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;

    // Default constructor
    public EmployeeDto() {}

    // Constructor without ID (for creation)
    public EmployeeDto(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // Full constructor
    public EmployeeDto(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
