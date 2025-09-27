package org.sanjeevas.springrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sanjeevas.springrest.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for EmployeeController
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.h2.console.enabled=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Employee Controller Integration Tests")
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        // Add test data
        employeeRepository.save(new Employee("John Doe", "Software Engineer"));
        employeeRepository.save(new Employee("Jane Smith", "Product Manager"));
    }

    @Test
    @DisplayName("Should get all employees with pagination")
    void getAllEmployees_ShouldReturnPaginatedEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "name")
                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.embedded.employeeList", hasSize(2)))
                .andExpect(jsonPath("$.embedded.employeeList[0].name", is("Jane Smith"))) // First alphabetically
                .andExpect(jsonPath("$.embedded.employeeList[1].name", is("John Doe")))
                .andExpect(jsonPath("$.page.size", is(10)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$.page.totalElements", is(2)));
    }

    @Test
    @DisplayName("Should get employee by ID")
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() throws Exception {
        Employee savedEmployee = employeeRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/employees/{id}", savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedEmployee.getId().intValue())))
                .andExpect(jsonPath("$.name", is(savedEmployee.getName())))
                .andExpect(jsonPath("$.role", is(savedEmployee.getRole())));
    }

    @Test
    @DisplayName("Should return 404 when employee not found")
    void getEmployeeById_WhenEmployeeNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("EMPLOYEE_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Employee not found")))
                .andExpect(jsonPath("$.traceId", notNullValue()));
    }

    @Test
    @DisplayName("Should create new employee")
    @Transactional
    void createEmployee_WithValidData_ShouldCreateEmployee() throws Exception {
        EmployeeDto newEmployee = new EmployeeDto("Alice Johnson", "UX Designer");

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Alice Johnson")))
                .andExpect(jsonPath("$.role", is("UX Designer")))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Should return 400 for invalid employee data")
    void createEmployee_WithInvalidData_ShouldReturn400() throws Exception {
        EmployeeDto invalidEmployee = new EmployeeDto("", ""); // Invalid: empty fields

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Should update existing employee")
    @Transactional
    void updateEmployee_WhenEmployeeExists_ShouldUpdateEmployee() throws Exception {
        Employee existingEmployee = employeeRepository.findAll().get(0);
        EmployeeDto updateDto = new EmployeeDto("Updated Name", "Updated Role");

        mockMvc.perform(put("/api/v1/employees/{id}", existingEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(existingEmployee.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.role", is("Updated Role")));
    }

    @Test
    @DisplayName("Should create new employee when updating non-existent employee")
    @Transactional
    void updateEmployee_WhenEmployeeNotExists_ShouldCreateEmployee() throws Exception {
        EmployeeDto updateDto = new EmployeeDto("New Employee", "New Role");

        mockMvc.perform(put("/api/v1/employees/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("New Employee")))
                .andExpect(jsonPath("$.role", is("New Role")))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Should partially update employee")
    @Transactional
    void partialUpdateEmployee_WhenEmployeeExists_ShouldUpdateEmployee() throws Exception {
        Employee existingEmployee = employeeRepository.findAll().get(0);
        EmployeeDto partialUpdateDto = new EmployeeDto();
        partialUpdateDto.setName("Partially Updated Name");

        mockMvc.perform(patch("/api/v1/employees/{id}", existingEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partialUpdateDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(existingEmployee.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Partially Updated Name")))
                .andExpect(jsonPath("$.role", is(existingEmployee.getRole()))); // Role should remain unchanged
    }

    @Test
    @DisplayName("Should delete employee")
    @Transactional
    void deleteEmployee_WhenEmployeeExists_ShouldDeleteEmployee() throws Exception {
        Employee existingEmployee = employeeRepository.findAll().get(0);

        mockMvc.perform(delete("/api/v1/employees/{id}", existingEmployee.getId())
                .with(csrf()))
                .andExpect(status().isNoContent());

        // Verify employee is deleted
        mockMvc.perform(get("/api/v1/employees/{id}", existingEmployee.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent employee")
    void deleteEmployee_WhenEmployeeNotExists_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", 999L)
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("EMPLOYEE_NOT_FOUND")));
    }

    @Test
    @DisplayName("Should search employees by name")
    void searchEmployeesByName_ShouldReturnMatchingEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees/search")
                .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.embedded.employeeList", hasSize(1)))
                .andExpect(jsonPath("$.embedded.employeeList[0].name", containsString("John")));
    }

    @Test
    @DisplayName("Should get employees by role")
    void getEmployeesByRole_ShouldReturnEmployeesWithRole() throws Exception {
        mockMvc.perform(get("/api/v1/employees/role/{role}", "Software Engineer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.embedded.employeeList", hasSize(1)))
                .andExpect(jsonPath("$.embedded.employeeList[0].role", is("Software Engineer")));
    }

    @Test
    @DisplayName("Should get employee count")
    void getEmployeeCount_ShouldReturnTotalCount() throws Exception {
        mockMvc.perform(get("/api/v1/employees/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("2")); // Two employees in test data
    }

    @Test
    @DisplayName("Should return 401 without authentication")
    @WithMockUser(roles = {}) // No roles
    void getAllEmployees_WithoutAuthentication_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should handle malformed JSON")
    void createEmployee_WithMalformedJson_ShouldReturn400() throws Exception {
        String malformedJson = "{\"name\": \"John\", \"role\":}"; // Missing value

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson)
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("MALFORMED_JSON")));
    }

    @Test
    @DisplayName("Should handle invalid path variable type")
    void getEmployeeById_WithInvalidIdType_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{id}", "invalid-id"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("TYPE_MISMATCH")));
    }
}
