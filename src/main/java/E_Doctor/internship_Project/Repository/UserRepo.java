package E_Doctor.internship_Project.Repository;

import E_Doctor.internship_Project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
   Optional<User> findByUsername(String username);
    
   
   Optional <User> findByEmail(String email);

	
}