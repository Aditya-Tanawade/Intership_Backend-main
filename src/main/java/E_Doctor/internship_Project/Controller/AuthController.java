package E_Doctor.internship_Project.Controller;



import E_Doctor.internship_Project.Advices.ApiError;
import E_Doctor.internship_Project.DTO.RegisterUserDTo;
import E_Doctor.internship_Project.Entity.User;
import E_Doctor.internship_Project.Repository.UserRepo;
import E_Doctor.internship_Project.DTO.LoginRequest;
import E_Doctor.internship_Project.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiError> registerUser(@RequestBody @Valid RegisterUserDTo userDto) {
        return userService.registerNewUser(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (isAuthenticated) {
            ApiError successResponse = ApiError.builder()
                    .status(HttpStatus.OK)
                    .message("Login Successful")
                    .build();
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } else {
            ApiError errorResponse = ApiError.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Invalid username or password")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    
    }
}

