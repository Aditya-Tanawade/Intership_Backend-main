package E_Doctor.internship_Project.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

    @Entity
    @Data
    @Table(name="doctors")
    public class Doctor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int doctorId;

        private String name;

        private String speciality;

        private String location;

        private String hospitalName;

        private String mobileNo;

        @Email
        @Column(unique = true, nullable = false)
        private String email;

        private String password;

}
