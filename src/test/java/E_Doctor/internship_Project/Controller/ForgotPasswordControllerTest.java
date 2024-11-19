package E_Doctor.internship_Project.Controller;

import E_Doctor.internship_Project.Entity.User;
import E_Doctor.internship_Project.Repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordControllerTest {

    @Mock
    private UserRepo userRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setOtp("1234");
    }

    @Test
    public void testForgotPassword_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = forgotPasswordController.forgotPassword(Map.of("email", "test@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP sent to your email.", response.getBody());

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify email was sent
    }

    @Test
    public void testForgotPassword_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            forgotPasswordController.forgotPassword(Map.of("email", "nonexistent@example.com"));
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testVerifyEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = forgotPasswordController.verifyEmail(Map.of("email", "test@example.com", "otp", "1234"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP verified. Please set your new password.", response.getBody());
    }

    @Test
    public void testVerifyEmail_InvalidOtp() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = forgotPasswordController.verifyEmail(Map.of("email", "test@example.com", "otp", "0000"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid OTP", response.getBody());
    }

    @Test
    public void testResetPassword_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedPassword");

        ResponseEntity<String> response = forgotPasswordController.resetPassword(Map.of("email", "test@example.com", "newPassword", "newpassword"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successfully.", response.getBody());
        assertEquals("encodedPassword", user.getPassword()); // Ensure password is encoded
        assertNull(user.getOtp()); // Ensure OTP is cleared after reset
    }

    @Test
    public void testResetPassword_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            forgotPasswordController.resetPassword(Map.of("email", "nonexistent@example.com", "newPassword", "newpassword"));
        });

        assertEquals("User not found", exception.getMessage());
    }
}

