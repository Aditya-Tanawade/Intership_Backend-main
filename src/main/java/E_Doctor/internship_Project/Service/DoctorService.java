package E_Doctor.internship_Project.Service;

import E_Doctor.internship_Project.DTO.DoctorDTO;
import E_Doctor.internship_Project.Entity.Doctor;
import E_Doctor.internship_Project.Repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
//    @Autowired
//    private DoctorRepo doctorRepo;
//
//    public Doctor createDoctor(Doctor doctor) {
//        return doctorRepo.save(doctor);
//    }
//
//    public List<Doctor> getAllDoctors(){
//        return doctorRepo.findAll();
//
//
//    }
//    public void deleteDoctor(Long doctorId) {
//        doctorRepo.deleteById(doctorId);
//    }
@Autowired
private DoctorRepo doctorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        // Map DTO to Entity
        Doctor doctor = new Doctor();
        doctor.setName(doctorDTO.getName());
        doctor.setSpeciality(doctorDTO.getSpeciality());
        doctor.setLocation(doctorDTO.getLocation());
        doctor.setHospitalName(doctorDTO.getHospitalName());
        doctor.setMobileNo(doctorDTO.getMobileNo());
        doctor.setEmail(doctorDTO.getEmail());

        //doctor.setPassword(doctorDTO.getPassword());

        doctor.setPassword(passwordEncoder.encode(doctorDTO.getPassword()));

        // Save to database
        Doctor savedDoctor = doctorRepo.save(doctor);

        // Map Entity back to DTO
        return new DoctorDTO(
                savedDoctor.getName(),
                savedDoctor.getSpeciality(),
                savedDoctor.getLocation(),
                savedDoctor.getHospitalName(),
                savedDoctor.getMobileNo(),
                savedDoctor.getEmail(),
                savedDoctor.getPassword()
        );
    }

    public List<DoctorDTO> getAllDoctors() {
        return doctorRepo.findAll().stream()
                .map(doctor -> new DoctorDTO(

                        doctor.getName(),
                        doctor.getSpeciality(),
                        doctor.getLocation(),
                        doctor.getHospitalName(),
                        doctor.getMobileNo(),
                        doctor.getEmail(),
                        doctor.getPassword()

                ))
                .collect(Collectors.toList());
    }
    public void deleteDoctor(Long id) {
        if (!doctorRepo.existsById(id)) {
            throw new RuntimeException("Doctor with ID " + id + " does not exist.");
        }
        doctorRepo.deleteById(id);
    }

}
