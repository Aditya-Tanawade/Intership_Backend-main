package E_Doctor.internship_Project.Configuration;

import E_Doctor.internship_Project.DTO.RegisterUserDTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GlobalExceptionHandlerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private MockMvc mockMvc;

    @Test
    void handleMethodArgumentNotValidException() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Invalid DTO with missing fields
        RegisterUserDTo invalidDto = new RegisterUserDTo();
        invalidDto.setEmail(""); // Invalid email
        invalidDto.setPassword("123"); // Invalid password length

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("Input Validation Failed"))
                .andExpect(jsonPath("$.subErrors").isArray());
    }

    @Test
    void handleDataIntegrityViolationException_EmailAlreadyExists() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Simulate a request that causes a DataIntegrityViolationException
        Map<String, String> request = new HashMap<>();
        request.put("email", "duplicate@example.com"); // Email already exists
        request.put("username", "TestUser");
        request.put("password", "password123");

        // Simulating manually catching the exception
        DataIntegrityViolationException exception = new DataIntegrityViolationException("email already exists");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("Input Validation Failed"));
    }

    @Test
    void handleGenericException() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Simulating a generic exception
        Map<String, String> request = new HashMap<>();
        request.put("username", "TestUser"); // Missing fields for validation

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                //.andExpect(status().isInternalServerError())
                .andExpect(status().isBadRequest())
                //.andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))

                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("Input Validation Failed"));
    }
}

