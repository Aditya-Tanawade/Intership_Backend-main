package E_Doctor.internship_Project.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class RegisterUserDTo {
	
	//add email field
    @NotBlank(message = "Email is required")
    @Email
     private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|USER|DOCTOR)$", message = "Role can be USER or ADMIN or DOCTOR")
    private String role;

}