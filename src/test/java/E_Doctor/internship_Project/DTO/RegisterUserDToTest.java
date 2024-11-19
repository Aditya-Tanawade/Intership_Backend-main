package E_Doctor.internship_Project.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserDTOTest {

    private RegisterUserDTo registerUserDTO;
    private Validator validator;

    @BeforeEach
    void setUp() {
        registerUserDTO = new RegisterUserDTo();
        validator = new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
        ((org.springframework.validation.beanvalidation.LocalValidatorFactoryBean) validator).afterPropertiesSet();
    }

    @Test
    void testValidRegisterUserDTO() {
        registerUserDTO.setEmail("test@example.com");
        registerUserDTO.setUsername("testuser");
        registerUserDTO.setPassword("password123");
        registerUserDTO.setConfirmPassword("password123");
        registerUserDTO.setRole("USER");

        Errors errors = new BeanPropertyBindingResult(registerUserDTO, "registerUserDTO");
        validator.validate(registerUserDTO, errors);

        assertFalse(errors.hasErrors(), "There should be no validation errors for valid input");
    }

    @Test
    void testInvalidEmail() {
        registerUserDTO.setEmail("invalid-email");
        registerUserDTO.setUsername("testuser");
        registerUserDTO.setPassword("password123");
        registerUserDTO.setConfirmPassword("password123");
        registerUserDTO.setRole("USER");

        Errors errors = new BeanPropertyBindingResult(registerUserDTO, "registerUserDTO");
        validator.validate(registerUserDTO, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist for invalid email");
        assertEquals("must be a well-formed email address", errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    void testShortUsername() {
        registerUserDTO.setEmail("test@example.com");
        registerUserDTO.setUsername("tu");
        registerUserDTO.setPassword("password123");
        registerUserDTO.setConfirmPassword("password123");
        registerUserDTO.setRole("USER");

        Errors errors = new BeanPropertyBindingResult(registerUserDTO, "registerUserDTO");
        validator.validate(registerUserDTO, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist for short username");
        assertEquals("Username must be between 3 and 50 characters", errors.getFieldError("username").getDefaultMessage());
    }

    @Test
    void testShortPassword() {
        registerUserDTO.setEmail("test@example.com");
        registerUserDTO.setUsername("testuser");
        registerUserDTO.setPassword("12345");
        registerUserDTO.setConfirmPassword("12345");
        registerUserDTO.setRole("USER");

        Errors errors = new BeanPropertyBindingResult(registerUserDTO, "registerUserDTO");
        validator.validate(registerUserDTO, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist for short password");
        assertEquals("Password must be at least 6 characters long", errors.getFieldError("password").getDefaultMessage());
    }

    @Test
    void testInvalidRole() {
        registerUserDTO.setEmail("test@example.com");
        registerUserDTO.setUsername("testuser");
        registerUserDTO.setPassword("password123");
        registerUserDTO.setConfirmPassword("password123");
        registerUserDTO.setRole("ADMIN");

        Errors errors = new BeanPropertyBindingResult(registerUserDTO, "registerUserDTO");
        validator.validate(registerUserDTO, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist for invalid role");
        assertEquals("Role can be USER or DOCTOR", errors.getFieldError("role").getDefaultMessage());
    }

    @Test
    void testAllFieldsMissing() {
        registerUserDTO.setEmail("");
        registerUserDTO.setUsername("");
        registerUserDTO.setPassword("");
        registerUserDTO.setConfirmPassword("");
        registerUserDTO.setRole("");

        Errors errors = new BeanPropertyBindingResult(registerUserDTO, "registerUserDTO");
        validator.validate(registerUserDTO, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist for all missing fields");
        assertEquals(7, errors.getErrorCount(), "There should be 7 validation errors");
    }
}

