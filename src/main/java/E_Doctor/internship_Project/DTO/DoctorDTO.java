package E_Doctor.internship_Project.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {


    @NotBlank(message = "Doctor Name is required")
    private String name;

    @NotBlank(message = "Speciality is required")
    private String speciality;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Hospital Name is required")
    private String hospitalName;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile Number must be a 10-digit number")
    private String mobileNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

}
