package org.sanjeevas.springrest;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Employee entity with auditing capabilities and validation
 * 
 * @author Sanjeeva
 * @version 2.0
 */
@Entity
@Table(name = "employees", 
       indexes = {
           @Index(name = "idx_employee_name", columnList = "name"),
           @Index(name = "idx_employee_role", columnList = "role")
       })
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "Employee name cannot be blank")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Employee role cannot be blank")
    @Size(min = 2, max = 50, message = "Employee role must be between 2 and 50 characters")
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "version")
    @Version
    private Long version;

    // Default constructor required by JPA and MapStruct
    public Employee() {}

    // Constructor for creating new employees
    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // Full constructor for testing
    public Employee(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = org.sanjeevas.springrest.security.SecurityUtils.validateAndSanitizeName(name);
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = org.sanjeevas.springrest.security.SecurityUtils.validateAndSanitizeRole(role);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // Business methods
    public void updateDetails(String name, String role) {
        this.name = org.sanjeevas.springrest.security.SecurityUtils.validateAndSanitizeName(name);
        this.role = org.sanjeevas.springrest.security.SecurityUtils.validateAndSanitizeRole(role);
    }

    public boolean hasRole(String role) {
        if (this.role == null || role == null) {
            return false;
        }
        String normalizedThisRole = org.sanjeevas.springrest.security.SecurityUtils.normalizeUnicode(this.role);
        String normalizedInputRole = org.sanjeevas.springrest.security.SecurityUtils.normalizeUnicode(role);
        return normalizedThisRole.equalsIgnoreCase(normalizedInputRole);
    }

    public boolean isActive() {
        return this.name != null && !this.name.trim().isEmpty() && 
               this.role != null && !this.role.trim().isEmpty();
    }

    // PrePersist and PreUpdate callbacks
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Employee{" + 
               "id=" + id + 
               ", name='" + name + '\'' + 
               ", role='" + role + '\'' + 
               ", createdAt=" + createdAt + 
               ", updatedAt=" + updatedAt + 
               ", version=" + version + 
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        
        // For entities with generated IDs, use business equality when ID is null
        if (this.id == null || employee.id == null) {
            return Objects.equals(this.name, employee.name) && 
                   Objects.equals(this.role, employee.role);
        }
        
        return Objects.equals(this.id, employee.id);
    }

    @Override
    public int hashCode() {
        // Use business fields for hash when ID is null (new entities)
        if (this.id == null) {
            return Objects.hash(this.name, this.role);
        }
        return Objects.hash(this.id);
    }
}
