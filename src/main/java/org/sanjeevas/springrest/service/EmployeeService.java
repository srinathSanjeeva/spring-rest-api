package org.sanjeevas.springrest.service;

import org.sanjeevas.springrest.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Employee operations
 * 
 * @author Sanjeeva
 * @version 1.0
 */
public interface EmployeeService {

    /**
     * Retrieve all employees
     * 
     * @return List of all employees
     */
    List<EmployeeDto> findAll();

    /**
     * Retrieve employees with pagination
     * 
     * @param pageable Pagination information
     * @return Page of employees
     */
    Page<EmployeeDto> findAll(Pageable pageable);

    /**
     * Find employee by ID
     * 
     * @param id Employee ID
     * @return Optional EmployeeDto
     */
    Optional<EmployeeDto> findById(Long id);

    /**
     * Create a new employee
     * 
     * @param employeeDto Employee data
     * @return Created employee
     */
    EmployeeDto create(EmployeeDto employeeDto);

    /**
     * Update an existing employee
     * 
     * @param id Employee ID
     * @param employeeDto Updated employee data
     * @return Updated employee
     */
    EmployeeDto update(Long id, EmployeeDto employeeDto);

    /**
     * Partially update an existing employee
     * 
     * @param id Employee ID
     * @param employeeDto Partial employee data
     * @return Updated employee
     */
    EmployeeDto partialUpdate(Long id, EmployeeDto employeeDto);

    /**
     * Delete employee by ID
     * 
     * @param id Employee ID
     * @return true if deleted, false if not found
     */
    boolean deleteById(Long id);

    /**
     * Check if employee exists by ID
     * 
     * @param id Employee ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Search employees by name (case-insensitive)
     * 
     * @param name Name to search for
     * @return List of matching employees
     */
    List<EmployeeDto> findByNameContainingIgnoreCase(String name);

    /**
     * Find employees by role
     * 
     * @param role Role to search for
     * @return List of employees with the specified role
     */
    List<EmployeeDto> findByRole(String role);

    /**
     * Get total count of employees
     * 
     * @return Total number of employees
     */
    long count();
}
