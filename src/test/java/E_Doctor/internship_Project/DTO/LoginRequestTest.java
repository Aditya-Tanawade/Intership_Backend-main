package E_Doctor.internship_Project.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        validator = new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
        ((org.springframework.validation.beanvalidation.LocalValidatorFactoryBean) validator).afterPropertiesSet();
    }

    @Test
    void testValidLoginRequest() {
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        Errors errors = new BeanPropertyBindingResult(loginRequest, "loginRequest");
        validator.validate(loginRequest, errors);

        assertFalse(errors.hasErrors(), "There should be no validation errors for valid input");
    }

    @Test
    void testMissingEmail() {
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");

        Errors errors = new BeanPropertyBindingResult(loginRequest, "loginRequest");
        validator.validate(loginRequest, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist when email is missing");
        assertEquals("Email is required", errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    void testMissingPassword() {
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("");

        Errors errors = new BeanPropertyBindingResult(loginRequest, "loginRequest");
        validator.validate(loginRequest, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist when password is missing");
        assertEquals("Password is required", errors.getFieldError("password").getDefaultMessage());
    }

    @Test
    void testAllFieldsMissing() {
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        Errors errors = new BeanPropertyBindingResult(loginRequest, "loginRequest");
        validator.validate(loginRequest, errors);

        assertTrue(errors.hasErrors(), "Validation errors should exist when all fields are missing");
        assertEquals(2, errors.getErrorCount(), "There should be 2 validation errors");
    }
}

