package E_Doctor.internship_Project.Repository;

import E_Doctor.internship_Project.Entity.Doctor;
import E_Doctor.internship_Project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {


   Optional< Doctor> findByEmail(String email);


}

