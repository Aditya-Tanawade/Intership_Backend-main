//package E_Doctor.internship_Project.DTO;
//
//import jakarta.persistence.Column;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import jakarta.validation.constraints.*;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class DoctorDTO {
//
//    private int doctorId;
//
//    @NotBlank(message = "Doctor name cannot be empty")
//    @Size(min = 2, max = 100, message = "Doctor name must be between 2 and 100 characters")
//    private String doctorName;
//
//    @NotBlank(message = "Speciality cannot be empty")
//    @Size(min = 2, max = 50, message = "Speciality must be between 2 and 50 characters")
//    private String speciality;
//
//    @NotBlank(message = "Location cannot be empty")
//    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
//    private String location;
//
//    @NotBlank(message = "Mobile number cannot be empty")
//    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
//    private String mobileNo;
//
//    @NotBlank(message = "Hospital name cannot be empty")
//    @Size(min = 2, max = 100, message = "Hospital name must be between 2 and 100 characters")
//    private String hospitalName;
//
//    @Positive(message = "Charge per visit must be greater than 0")
//    @Digits(integer = 6, fraction = 2, message = "Charge per visit must be a valid amount (max 6 digits and 2 decimal places)")
//    private double chargedPerVisit;
//}
