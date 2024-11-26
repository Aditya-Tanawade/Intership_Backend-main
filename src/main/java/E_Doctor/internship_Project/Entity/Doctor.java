package E_Doctor.internship_Project.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;

    //@Column(nullable = false)
    private String doctorName;

    //@Column(nullable = false)
    private String speciality;

    //@Column(nullable = false)
    private String location;

    //@Column(nullable = false, unique = true)
    private String mobileNo;

    //@Column(nullable = false, unique = true)
    private String email;

    //@Column(nullable = false)
    private String password;

    //@Column(nullable = false)
    private String hospitalName;

  //  @Column(nullable = false)
    private double chargedPerVisit;

}
