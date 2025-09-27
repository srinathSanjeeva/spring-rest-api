package org.sanjeevas.springrest.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sanjeevas.springrest.Employee;
import org.sanjeevas.springrest.EmployeeRepository;
import org.sanjeevas.springrest.dto.EmployeeDto;
import org.sanjeevas.springrest.mapper.EmployeeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeServiceImpl
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service Tests")
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee testEmployee;
    private EmployeeDto testEmployeeDto;
    private List<Employee> testEmployees;
    private List<EmployeeDto> testEmployeeDtos;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L, "John Doe", "Software Engineer");
        testEmployeeDto = new EmployeeDto(1L, "John Doe", "Software Engineer");
        
        Employee employee2 = new Employee(2L, "Jane Smith", "Product Manager");
        EmployeeDto employeeDto2 = new EmployeeDto(2L, "Jane Smith", "Product Manager");
        
        testEmployees = Arrays.asList(testEmployee, employee2);
        testEmployeeDtos = Arrays.asList(testEmployeeDto, employeeDto2);
    }

    @Test
    @DisplayName("Should return all employees")
    void findAll_ShouldReturnAllEmployees() {
        // Given
        when(employeeRepository.findAll()).thenReturn(testEmployees);
        when(employeeMapper.toDtoList(testEmployees)).thenReturn(testEmployeeDtos);

        // When
        List<EmployeeDto> result = employeeService.findAll();

        // Then
        assertThat(result)
                .hasSize(2)
                .isEqualTo(testEmployeeDtos);
        verify(employeeRepository).findAll();
        verify(employeeMapper).toDtoList(testEmployees);
    }

    @Test
    @DisplayName("Should return paginated employees")
    void findAllWithPagination_ShouldReturnPaginatedEmployees() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> employeePage = new PageImpl<>(testEmployees, pageable, testEmployees.size());
        
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
        when(employeeMapper.toDto(any(Employee.class)))
            .thenReturn(testEmployeeDto)
            .thenReturn(testEmployeeDtos.get(1));

        // When
        Page<EmployeeDto> result = employeeService.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(employeeRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return employee by ID when exists")
    void findById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Given
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(employeeMapper.toDto(testEmployee)).thenReturn(testEmployeeDto);

        // When
        Optional<EmployeeDto> result = employeeService.findById(employeeId);

        // Then
        assertThat(result).contains(testEmployeeDto);
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper).toDto(testEmployee);
    }

    @Test
    @DisplayName("Should return empty when employee does not exist")
    void findById_WhenEmployeeDoesNotExist_ShouldReturnEmpty() {
        // Given
        Long employeeId = 999L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When
        Optional<EmployeeDto> result = employeeService.findById(employeeId);

        // Then
        assertThat(result).isEmpty();
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should create new employee")
    void create_ShouldCreateNewEmployee() {
        // Given
        EmployeeDto newEmployeeDto = new EmployeeDto("New Employee", "Developer");
        Employee newEmployee = new Employee("New Employee", "Developer");
        Employee savedEmployee = new Employee(3L, "New Employee", "Developer");
        EmployeeDto savedEmployeeDto = new EmployeeDto(3L, "New Employee", "Developer");

        when(employeeMapper.toEntity(newEmployeeDto)).thenReturn(newEmployee);
        when(employeeRepository.save(newEmployee)).thenReturn(savedEmployee);
        when(employeeMapper.toDto(savedEmployee)).thenReturn(savedEmployeeDto);

        // When
        EmployeeDto result = employeeService.create(newEmployeeDto);

        // Then
        assertThat(result).isEqualTo(savedEmployeeDto);
        assertThat(result.getId()).isNotNull();
        verify(employeeMapper).toEntity(newEmployeeDto);
        verify(employeeRepository).save(newEmployee);
        verify(employeeMapper).toDto(savedEmployee);
    }

    @Test
    @DisplayName("Should update existing employee")
    void update_WhenEmployeeExists_ShouldUpdateEmployee() {
        // Given
        Long employeeId = 1L;
        EmployeeDto updateDto = new EmployeeDto("Updated Name", "Updated Role");
        Employee existingEmployee = new Employee(employeeId, "Old Name", "Old Role");
        Employee updatedEmployee = new Employee(employeeId, "Updated Name", "Updated Role");
        EmployeeDto updatedEmployeeDto = new EmployeeDto(employeeId, "Updated Name", "Updated Role");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);
        when(employeeMapper.toDto(updatedEmployee)).thenReturn(updatedEmployeeDto);

        // When
        EmployeeDto result = employeeService.update(employeeId, updateDto);

        // Then
        assertThat(result).isEqualTo(updatedEmployeeDto);
        assertThat(existingEmployee.getName()).isEqualTo("Updated Name");
        assertThat(existingEmployee.getRole()).isEqualTo("Updated Role");
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).save(existingEmployee);
        verify(employeeMapper).toDto(updatedEmployee);
    }

    @Test
    @DisplayName("Should create new employee when updating non-existent employee")
    void update_WhenEmployeeDoesNotExist_ShouldCreateNewEmployee() {
        // Given
        Long employeeId = 999L;
        EmployeeDto updateDto = new EmployeeDto("New Name", "New Role");
        Employee newEmployee = new Employee("New Name", "New Role");
        Employee savedEmployee = new Employee(employeeId, "New Name", "New Role");
        EmployeeDto savedEmployeeDto = new EmployeeDto(employeeId, "New Name", "New Role");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        when(employeeMapper.toEntity(updateDto)).thenReturn(newEmployee);
        when(employeeRepository.save(newEmployee)).thenReturn(savedEmployee);
        when(employeeMapper.toDto(savedEmployee)).thenReturn(savedEmployeeDto);

        // When
        EmployeeDto result = employeeService.update(employeeId, updateDto);

        // Then
        assertThat(result).isEqualTo(savedEmployeeDto);
        assertThat(newEmployee.getId()).isNull(); // ID should be null before saving
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper).toEntity(updateDto);
        verify(employeeRepository).save(newEmployee);
        verify(employeeMapper).toDto(savedEmployee);
    }

    @Test
    @DisplayName("Should delete employee when exists")
    void deleteById_WhenEmployeeExists_ShouldReturnTrue() {
        // Given
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // When
        boolean result = employeeService.deleteById(employeeId);

        // Then
        assertThat(result).isTrue();
        verify(employeeRepository).existsById(employeeId);
        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent employee")
    void deleteById_WhenEmployeeDoesNotExist_ShouldReturnFalse() {
        // Given
        Long employeeId = 999L;
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // When
        boolean result = employeeService.deleteById(employeeId);

        // Then
        assertThat(result).isFalse();
        verify(employeeRepository).existsById(employeeId);
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should check if employee exists")
    void existsById_ShouldCheckIfEmployeeExists() {
        // Given
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // When
        boolean result = employeeService.existsById(employeeId);

        // Then
        assertThat(result).isTrue();
        verify(employeeRepository).existsById(employeeId);
    }

    @Test
    @DisplayName("Should find employees by name containing")
    void findByNameContainingIgnoreCase_ShouldReturnMatchingEmployees() {
        // Given
        String searchName = "John";
        when(employeeRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(List.of(testEmployee));
        when(employeeMapper.toDtoList(List.of(testEmployee))).thenReturn(List.of(testEmployeeDto));

        // When
        List<EmployeeDto> result = employeeService.findByNameContainingIgnoreCase(searchName);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testEmployeeDto);
        verify(employeeRepository).findByNameContainingIgnoreCase(searchName);
        verify(employeeMapper).toDtoList(List.of(testEmployee));
    }

    @Test
    @DisplayName("Should find employees by role")
    void findByRole_ShouldReturnEmployeesWithRole() {
        // Given
        String role = "Software Engineer";
        when(employeeRepository.findByRole(role)).thenReturn(List.of(testEmployee));
        when(employeeMapper.toDtoList(List.of(testEmployee))).thenReturn(List.of(testEmployeeDto));

        // When
        List<EmployeeDto> result = employeeService.findByRole(role);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testEmployeeDto);
        verify(employeeRepository).findByRole(role);
        verify(employeeMapper).toDtoList(List.of(testEmployee));
    }

    @Test
    @DisplayName("Should return total count of employees")
    void count_ShouldReturnTotalCount() {
        // Given
        long expectedCount = 5L;
        when(employeeRepository.count()).thenReturn(expectedCount);

        // When
        long result = employeeService.count();

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(employeeRepository).count();
    }

    @Test
    @DisplayName("Should partially update employee when exists")
    void partialUpdate_WhenEmployeeExists_ShouldUpdateEmployee() {
        // Given
        Long employeeId = 1L;
        EmployeeDto partialUpdateDto = new EmployeeDto();
        partialUpdateDto.setName("Partially Updated Name");
        
        Employee existingEmployee = new Employee(employeeId, "Old Name", "Old Role");
        Employee updatedEmployee = new Employee(employeeId, "Partially Updated Name", "Old Role");
        EmployeeDto updatedEmployeeDto = new EmployeeDto(employeeId, "Partially Updated Name", "Old Role");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);
        when(employeeMapper.toDto(updatedEmployee)).thenReturn(updatedEmployeeDto);

        // When
        EmployeeDto result = employeeService.partialUpdate(employeeId, partialUpdateDto);

        // Then
        assertThat(result)
                .isNotNull()
                .isEqualTo(updatedEmployeeDto);
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper).updateEntityFromDto(partialUpdateDto, existingEmployee);
        verify(employeeRepository).save(existingEmployee);
        verify(employeeMapper).toDto(updatedEmployee);
    }

    @Test
    @DisplayName("Should return null when partially updating non-existent employee")
    void partialUpdate_WhenEmployeeDoesNotExist_ShouldReturnNull() {
        // Given
        Long employeeId = 999L;
        EmployeeDto partialUpdateDto = new EmployeeDto();
        partialUpdateDto.setName("Some Name");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When
        EmployeeDto result = employeeService.partialUpdate(employeeId, partialUpdateDto);

        // Then
        assertThat(result).isNull();
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper, never()).updateEntityFromDto(any(), any());
        verify(employeeRepository, never()).save(any());
    }
}
