package E_Doctor.internship_Project.Controller;



import E_Doctor.internship_Project.Advices.ApiError;
import E_Doctor.internship_Project.DTO.RegisterUserDTo;
import E_Doctor.internship_Project.Entity.User;
import E_Doctor.internship_Project.DTO.LoginRequest;
import E_Doctor.internship_Project.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/loadAdmins")
      public String loadAdmins() {
         userService.loadUsers();
        return  "Admins Loaded Successfully";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiError> registerUser(@RequestBody @Valid RegisterUserDTo userDto) {
        return userService.registerNewUser(userDto);
    }


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        Optional<User> user = userService.findByEmail(loginRequest.getEmail());
//
//        if (user !=null) {
//            String userRole = userService.getUserRoleByEmail(loginRequest.getEmail());
//            boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//
//            if (isAuthenticated) {
//                // Check role and respond accordingly
//                if ("ADMIN".equals(userRole)) {
//                    ApiError successResponse = ApiError.builder()
//                            .status(HttpStatus.OK)
//                            .message("Admin login successful")
//                            .build();
//                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
//                } else if ("DOCTOR".equals(userRole)) {
//                    ApiError successResponse = ApiError.builder()
//                            .status(HttpStatus.OK)
//                            .message("Doctor login successful")
//                            .build();
//                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
//                } else if ("USER".equals(userRole)) {
//                    ApiError successResponse = ApiError.builder()
//                            .status(HttpStatus.OK)
//                            .message("User login successful")
//                            .build();
//                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
//                } else {
//                    ApiError errorResponse = ApiError.builder()
//                            .status(HttpStatus.UNAUTHORIZED)
//                            .message("Unauthorized role")
//                            .build();
//                    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//                }
//            } else {
//                ApiError errorResponse = ApiError.builder()
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .message("Invalid password")
//                        .build();
//                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//            }
//        } else {
//            ApiError errorResponse = ApiError.builder()
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .message("User not found")
//                    .build();
//            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//        }
//    }


    @PostMapping("/login")
    public ResponseEntity<ApiError> login(@RequestBody LoginRequest loginRequest) {
        ApiError apiError = userService.authenticateUser(loginRequest);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }



//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody  LoginRequest loginRequest) {
//        boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//
//        if (isAuthenticated) {
//            ApiError successResponse = ApiError.builder()
//                    .status(HttpStatus.OK)
//                    .message("Login Successful")
//                    .build();
//            return new ResponseEntity<>(successResponse, HttpStatus.OK);
//        } else {
//            ApiError errorResponse = ApiError.builder()
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .message("Invalid username or password")
//                    .build();
//            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//        }


//    @PostMapping("/admin-login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        Optional<User> user = userService.findByEmail(loginRequest.getEmail());
//        String userRole = userService.getUserRoleByEmail(loginRequest.getEmail());
//
//        if (user != null && userRole != "ADMIN") {
//            boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//
//            if (isAuthenticated) {
//                ApiError successResponse = ApiError.builder()
//                        .status(HttpStatus.OK)
//                        .message("Login Successful")
//                        .build();
//                return new ResponseEntity<>(successResponse, HttpStatus.OK);
//            } else {
//                ApiError errorResponse = ApiError.builder()
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .message("Invalid Password")
//                        .build();
//                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//            }
//        } else {
//            ApiError errorResponse = ApiError.builder()
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .message("You Are Not AN Administrator")
//                    .build();
//            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//        }
//    }



}

