package org.sanjeevas.springrest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Employee entity
 * Extends JpaRepository to provide CRUD operations and custom queries
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find employees by name containing the given string (case-insensitive)
     * 
     * @param name Name to search for
     * @return List of employees matching the criteria
     */
    List<Employee> findByNameContainingIgnoreCase(String name);

    /**
     * Find employees by exact role match (case-insensitive)
     * 
     * @param role Role to search for
     * @return List of employees with the specified role
     */
    List<Employee> findByRoleIgnoreCase(String role);

    /**
     * Find employees by role using custom query
     * 
     * @param role Role to search for
     * @return List of employees with the specified role
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.role) = LOWER(:role)")
    List<Employee> findByRole(@Param("role") String role);

    /**
     * Find employees by name and role
     * 
     * @param name Name to search for
     * @param role Role to search for
     * @return List of employees matching both criteria
     */
    List<Employee> findByNameContainingIgnoreCaseAndRoleIgnoreCase(String name, String role);

    /**
     * Count employees by role
     * 
     * @param role Role to count
     * @return Number of employees with the specified role
     */
    long countByRoleIgnoreCase(String role);

    /**
     * Check if employee exists by name
     * 
     * @param name Name to check
     * @return true if employee exists with the given name
     */
    boolean existsByNameIgnoreCase(String name);
}
