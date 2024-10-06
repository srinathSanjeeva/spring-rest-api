import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test for getting all users (GET /api/users)
     */
    @Test
    public void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Replace '2' with the expected number of users
                .andExpect(jsonPath("$[0].name", is("John Doe"))); // Replace 'John Doe' with an expected name
    }

    /**
     * Test for getting a user by ID (GET /api/users/{id})
     */
    @Test
    public void shouldReturnUserById() throws Exception {
        mockMvc.perform(get("/api/users/1")) // Use a valid user ID
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe"))) // Replace 'John Doe' with an expected name
                .andExpect(jsonPath("$.email", is("john.doe@example.com"))); // Expected email
    }

    /**
     * Test for creating a new user (POST /api/users)
     */
    @Test
    public void shouldCreateNewUser() throws Exception {
        // Define a new user
        User newUser = new User("Jane Doe", "jane.doe@example.com");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane Doe")))
                .andExpect(jsonPath("$.email", is("jane.doe@example.com")));
    }

    /**
     * Test for updating a user (PUT /api/users/{id})
     */
    @Test
    public void shouldUpdateUser() throws Exception {
        // Define the updated user information
        User updatedUser = new User("John Updated", "john.updated@example.com");

        mockMvc.perform(put("/api/users/1") // Update user with ID 1
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Updated")))
                .andExpect(jsonPath("$.email", is("john.updated@example.com")));
    }

    /**
     * Test for deleting a user (DELETE /api/users/{id})
     */
    @Test
    public void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")) // Delete user with ID 1
                .andExpect(status().isOk());
    }
}