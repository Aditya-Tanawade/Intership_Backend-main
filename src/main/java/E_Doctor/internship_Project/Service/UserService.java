package E_Doctor.internship_Project.Service;

import E_Doctor.internship_Project.Advices.ApiError;
import E_Doctor.internship_Project.DTO.LoginRequest;
import E_Doctor.internship_Project.DTO.RegisterUserDTo;
import E_Doctor.internship_Project.Entity.User;
import E_Doctor.internship_Project.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service

public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<ApiError> registerNewUser(RegisterUserDTo userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            ApiError apiError = ApiError.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Passwords do not match")
                    .build();
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        //save email also
        user.setEmail(userDto.getEmail());

        userRepo.save(user);

        ApiError successResponse = ApiError.builder()
                .status(HttpStatus.OK)
                .message("Registered Successfully")
                .build();
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }




    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public boolean authenticate(String email, String password) {
        // Retrieve the user by username
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Compare the provided password with the stored password (hashed)
            return passwordEncoder.matches(password, user.getPassword());
        }

        return false;  // User not found
    }




	


}
