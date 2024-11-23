package E_Doctor.internship_Project.Controller;

import E_Doctor.internship_Project.DTO.LoginRequest;
import E_Doctor.internship_Project.DTO.RegisterUserDTo;
import E_Doctor.internship_Project.Service.UserService;
import E_Doctor.internship_Project.Advices.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController; // Inject mocks into the controller.

    @Mock
    private UserService userService; // Mock the UserService dependency.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the mocks.
    }

    @Test
    void testLoadAdmins() {

        // Act
        String result = authController.loadAdmins();

        // Assert
        assertEquals("Admins Loaded Successfully", result);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegisterUserDTo userDto = new RegisterUserDTo();
        ApiError apiError = new ApiError(HttpStatus.CREATED, "User registered successfully", Collections.emptyList());
        ResponseEntity<ApiError> expectedResponse = new ResponseEntity<>(apiError, HttpStatus.CREATED);

        when(userService.registerNewUser(any(RegisterUserDTo.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiError> response = authController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    void testRegisterUser_ValidationError() {
        // Arrange
        RegisterUserDTo userDto = new RegisterUserDTo(); // Assume this triggers validation error
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error", Collections.emptyList());
        ResponseEntity<ApiError> expectedResponse = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

        when(userService.registerNewUser(any(RegisterUserDTo.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiError> response = authController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody().getMessage());
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        ApiError apiError = new ApiError(HttpStatus.OK, "Login successful", Collections.emptyList());
        when(userService.authenticateUser(any(LoginRequest.class))).thenReturn(apiError);

        // Act
        ResponseEntity<ApiError> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody().getMessage());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Invalid credentials", Collections.emptyList());
        when(userService.authenticateUser(any(LoginRequest.class))).thenReturn(apiError);

        // Act
        ResponseEntity<ApiError> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }
}

