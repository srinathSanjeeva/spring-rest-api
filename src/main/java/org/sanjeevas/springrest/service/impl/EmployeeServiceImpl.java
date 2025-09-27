package org.sanjeevas.springrest.service.impl;

import org.sanjeevas.springrest.Employee;
import org.sanjeevas.springrest.EmployeeRepository;
import org.sanjeevas.springrest.dto.EmployeeDto;
import org.sanjeevas.springrest.mapper.EmployeeMapper;
import org.sanjeevas.springrest.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of EmployeeService with caching and transaction management
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "employees", key = "'all'")
    public List<EmployeeDto> findAll() {
        logger.debug("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Retrieved {} employees", employees.size());
        return employeeMapper.toDtoList(employees);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> findAll(Pageable pageable) {
        logger.debug("Fetching employees with pagination: page={}, size={}", 
                    pageable.getPageNumber(), pageable.getPageSize());
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        logger.info("Retrieved {} employees from page {} of {}", 
                   employeePage.getNumberOfElements(), 
                   employeePage.getNumber(), 
                   employeePage.getTotalPages());
        return employeePage.map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "employee", key = "#id")
    public Optional<EmployeeDto> findById(Long id) {
        logger.debug("Fetching employee with id: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            logger.info("Employee found with id: {}", id);
            return Optional.of(employeeMapper.toDto(employee.get()));
        } else {
            logger.warn("Employee not found with id: {}", id);
            return Optional.empty();
        }
    }

    @Override
    @CacheEvict(value = "employees", key = "'all'")
    public EmployeeDto create(EmployeeDto employeeDto) {
        logger.debug("Creating new employee: {}", employeeDto);
        
        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        
        logger.info("Employee created successfully with id: {}", savedEmployee.getId());
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    @Caching(
        put = @CachePut(value = "employee", key = "#id"),
        evict = @CacheEvict(value = "employees", key = "'all'")
    )
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        logger.debug("Updating employee with id: {}, data: {}", id, employeeDto);
        
        return employeeRepository.findById(id)
            .map(existingEmployee -> {
                existingEmployee.setName(employeeDto.getName());
                existingEmployee.setRole(employeeDto.getRole());
                Employee updatedEmployee = employeeRepository.save(existingEmployee);
                logger.info("Employee updated successfully with id: {}", id);
                return employeeMapper.toDto(updatedEmployee);
            })
            .orElseGet(() -> {
                logger.info("Employee not found with id: {}, creating new employee", id);
                Employee newEmployee = employeeMapper.toEntity(employeeDto);
                Employee savedEmployee = employeeRepository.save(newEmployee);
                logger.info("Employee created successfully with id: {}", savedEmployee.getId());
                return employeeMapper.toDto(savedEmployee);
            });
    }

    @Override
    @Caching(
        put = @CachePut(value = "employee", key = "#id"),
        evict = @CacheEvict(value = "employees", key = "'all'")
    )
    public EmployeeDto partialUpdate(Long id, EmployeeDto employeeDto) {
        logger.debug("Partially updating employee with id: {}, data: {}", id, employeeDto);
        
        return employeeRepository.findById(id)
            .map(existingEmployee -> {
                employeeMapper.updateEntityFromDto(employeeDto, existingEmployee);
                Employee updatedEmployee = employeeRepository.save(existingEmployee);
                logger.info("Employee partially updated successfully with id: {}", id);
                return employeeMapper.toDto(updatedEmployee);
            })
            .orElse(null);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "employee", key = "#id"),
        @CacheEvict(value = "employees", key = "'all'")
    })
    public boolean deleteById(Long id) {
        logger.debug("Deleting employee with id: {}", id);
        
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            logger.info("Employee deleted successfully with id: {}", id);
            return true;
        } else {
            logger.warn("Employee not found for deletion with id: {}", id);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        logger.debug("Checking if employee exists with id: {}", id);
        boolean exists = employeeRepository.existsById(id);
        logger.debug("Employee exists with id {}: {}", id, exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findByNameContainingIgnoreCase(String name) {
        logger.debug("Searching employees by name containing: {}", name);
        List<Employee> employees = employeeRepository.findByNameContainingIgnoreCase(name);
        logger.info("Found {} employees matching name: {}", employees.size(), name);
        return employeeMapper.toDtoList(employees);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findByRole(String role) {
        logger.debug("Searching employees by role: {}", role);
        List<Employee> employees = employeeRepository.findByRole(role);
        logger.info("Found {} employees with role: {}", employees.size(), role);
        return employeeMapper.toDtoList(employees);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "employeeCount")
    public long count() {
        logger.debug("Counting total employees");
        long count = employeeRepository.count();
        logger.info("Total employee count: {}", count);
        return count;
    }
}
