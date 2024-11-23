package E_Doctor.internship_Project.Service;
import E_Doctor.internship_Project.Advices.ApiError;
import E_Doctor.internship_Project.DTO.LoginRequest;
import E_Doctor.internship_Project.DTO.RegisterUserDTo;
import E_Doctor.internship_Project.Entity.User;
import E_Doctor.internship_Project.Repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterNewUser_Success() {
        RegisterUserDTo registerUserDTo = new RegisterUserDTo("newUser", "newUser@gmail.com", "password", "password", "USER");

        when(userRepo.findByUsername(registerUserDTo.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserDTo.getPassword())).thenReturn("encodedPassword");

        ApiError response = userService.registerNewUser(registerUserDTo).getBody();

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Registered Successfully", response.getMessage());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterNewUser_EmailExists() {
        RegisterUserDTo registerUserDTo = new RegisterUserDTo("newUser", "newUser@gmail.com", "password", "password", "USER");

        when(userRepo.findByUsername(registerUserDTo.getEmail())).thenReturn(Optional.of(new User()));

        ApiError response = userService.registerNewUser(registerUserDTo).getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Email already exists", response.getMessage());
        verify(userRepo, times(0)).save(any(User.class));
    }

    @Test
    public void testRegisterNewUser_PasswordsDoNotMatch() {
        RegisterUserDTo registerUserDTo = new RegisterUserDTo("newUser", "newUser@gmail.com", "password", "differentPassword", "USER");

        ApiError response = userService.registerNewUser(registerUserDTo).getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Passwords do not match", response.getMessage());
    }


    @Test
    public void testAuthenticateUser_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest("user@gmail.com", "password");

        when(userRepo.findByUsername(loginRequest.getEmail())).thenReturn(Optional.empty());

        ApiError response = userService.authenticateUser(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        assertEquals("Invalid password", response.getMessage());
    }

    @Test
    public void testAuthenticateUser_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("user@gmail.com", "wrongPassword");
        User user = new User(1, "user", "user@gmail.com", "encodedPassword", "USER", "");

        when(userRepo.findByUsername(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        ApiError response = userService.authenticateUser(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        assertEquals("Invalid password", response.getMessage());
    }

    @Test
    public void testAuthenticateUser_UnauthorizedRole() {
        LoginRequest loginRequest = new LoginRequest("user@gmail.com", "password");
        User user = new User(1, "user", "user@gmail.com", "encodedPassword", "UNKNOWN_ROLE", "");

        when(userRepo.findByUsername(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);

        ApiError response = userService.authenticateUser(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        assertEquals("Invalid password", response.getMessage());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepo.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("user"));
    }
}

