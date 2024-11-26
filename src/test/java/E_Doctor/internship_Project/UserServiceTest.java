package E_Doctor.internship_Project;

import E_Doctor.internship_Project.Advices.ApiError;
import E_Doctor.internship_Project.DTO.LoginRequest;
import E_Doctor.internship_Project.DTO.RegisterUserDTo;
import E_Doctor.internship_Project.Entity.User;
import E_Doctor.internship_Project.Repository.UserRepo;
import E_Doctor.internship_Project.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUsers_shouldSaveAdmins() {
        userService.loadUsers();
        verify(userRepo, times(1)).saveAll(anyList());
    }

    @Test
    void registerNewUser_shouldReturnBadRequest_whenEmailExists() {
        RegisterUserDTo userDto = new RegisterUserDTo("John", "john@example.com", "password", "password", "USER");
        when(userRepo.findByUsername(userDto.getEmail())).thenReturn(Optional.of(new User()));

        ResponseEntity<ApiError> response = userService.registerNewUser(userDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody().getMessage());
    }

    @Test
    void registerNewUser_shouldReturnBadRequest_whenPasswordsDoNotMatch() {
        RegisterUserDTo userDto = new RegisterUserDTo("John", "john@example.com", "password1", "password2", "USER");
        when(userRepo.findByUsername(userDto.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<ApiError> response = userService.registerNewUser(userDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Passwords do not match", response.getBody().getMessage());
    }

    @Test
    void registerNewUser_shouldRegisterUserSuccessfully() {
        RegisterUserDTo userDto = new RegisterUserDTo("John", "john@example.com", "password", "password", "USER");
        when(userRepo.findByUsername(userDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<ApiError> response = userService.registerNewUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registered Successfully", response.getBody().getMessage());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void getUserRoleByEmail_shouldReturnRole_whenUserExists() {
        String email = "user@example.com";
        when(userRepo.findRoleByEmail(email)).thenReturn(Optional.of("USER"));

        String role = userService.getUserRoleByEmail(email);

        assertEquals("USER", role);
    }

    @Test
    void getUserRoleByEmail_shouldThrowException_whenUserNotFound() {
        String email = "user@example.com";
        when(userRepo.findRoleByEmail(email)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserRoleByEmail(email));
    }

    @Test
    void authenticate_shouldReturnTrue_whenPasswordMatches() {
        String email = "user@example.com";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";

        User user = new User(1, "user", email, encodedPassword, "USER", "");
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean isAuthenticated = userService.authenticate(email, rawPassword);

        assertTrue(isAuthenticated);
    }

    @Test
    void authenticate_shouldReturnFalse_whenPasswordDoesNotMatch() {
        String email = "user@example.com";
        String rawPassword = "wrongPassword";
        String encodedPassword = "encodedPassword";

        User user = new User(1, "user", email, encodedPassword, "USER", "");
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean isAuthenticated = userService.authenticate(email, rawPassword);

        assertFalse(isAuthenticated);
    }

    @Test
    void authenticateUser_shouldReturnUnauthorized_whenUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password");
        when(userRepo.findByUsername(loginRequest.getEmail())).thenReturn(Optional.empty());

        ApiError response = userService.authenticateUser(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void authenticateUser_shouldReturnSuccess_whenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest("admin@example.com", "password");
        User user = new User(1, "admin", "admin@example.com", "encodedPassword", "ADMIN", "");

        when(userRepo.findByUsername(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(userRepo.findRoleByEmail(loginRequest.getEmail())).thenReturn(Optional.of("ADMIN"));

        ApiError response = userService.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Admin login successful", response.getMessage());
    }
}
