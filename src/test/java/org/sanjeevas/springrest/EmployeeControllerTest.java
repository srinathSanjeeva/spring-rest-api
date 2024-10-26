package org.sanjeevas.springrest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.sanjeevas.springrest.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test for getting all employees (GET /api/employees)
     */
    @Test
    public void shouldReturnAllEmployees() throws Exception {
        mockMvc
            .perform(get("/employees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    /**
     * Test for getting an employee by ID (GET /api/employees/{id})
     */
    @Test
    public void shouldReturnEmployeeById() throws Exception {
        mockMvc
            .perform(get("/employees/2")) // Use a valid employee ID
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Jake Dunn"))) // Replace 'John' with an expected first name
            .andExpect(jsonPath("$.role", is("accomplice"))); // Expected email
    }

    /**
     * Test for creating a new employee (POST /api/employees)
     */
    @Test
    public void shouldCreateNewEmployee() throws Exception {
        // Define a new employee
        Employee newEmployee = new Employee("Jane Doe", "CEO");

        mockMvc
            .perform(
                post("/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newEmployee))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Jane Doe")))
            .andExpect(jsonPath("$.role", is("CEO")));
    }

    /**
     * Test for updating an employee (PUT /api/employees/{id})
     */
    @Test
    public void shouldUpdateEmployee() throws Exception {
        // Define the updated employee information
        Employee updatedEmployee = new Employee("John Doe", "Thief");

        mockMvc
            .perform(
                put("/employees/1") // Update employee with ID 1
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedEmployee))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Doe")))
            .andExpect(jsonPath("$.role", is("Thief")));
    }

    /**
     * Test for deleting an employee (DELETE /api/employees/{id})
     */
    @Test
    public void shouldDeleteEmployee() throws Exception {
        mockMvc
            .perform(delete("/employees/1")) // Delete employee with ID 1
            .andExpect(status().isOk());
    }
}
