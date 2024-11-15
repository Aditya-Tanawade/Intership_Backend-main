package E_Doctor.internship_Project.Controller;

import E_Doctor.internship_Project.Entity.User;
import E_Doctor.internship_Project.Repository.UserRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ForgotController {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;  // Inject JavaMailSender

    @Autowired
    public ForgotController(UserRepo userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepository.save(user);

        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        sendPasswordResetEmail(user);

        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
                                                @RequestParam("newPassword") String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword)); // Encoding the new password
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully.");
    }

    // Helper method to send password reset email
//    private void sendPasswordResetEmail(User user) {
//        String resetUrl = "http://localhost:8080/reset-password-form?email=" + user.getEmail();
//        String subject = "Password Reset Request of your E-Doctor Account";
//        String body = "Click the following link to reset your E-Doctor password: " + resetUrl;
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user.getEmail());
//        message.setSubject(subject);
//        message.setText(body);
//        mailSender.send(message);
//    }


    private void sendPasswordResetEmail(User user) {
        String resetUrl = "http://localhost:8080/reset-password-form?email=" + user.getEmail();
        String subject = "Password Reset Request of your E-Doctor Account";
        String body = "Click the following link to reset your E-Doctor password: " + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}



