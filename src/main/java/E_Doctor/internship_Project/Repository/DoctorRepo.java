package E_Doctor.internship_Project.Repository;

import E_Doctor.internship_Project.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctor, Long>
{
}
