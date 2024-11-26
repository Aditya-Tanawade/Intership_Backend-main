package E_Doctor.internship_Project.Repository;

import E_Doctor.internship_Project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    
   
   Optional <User> findByEmail(String email);

   @Query("SELECT u.role FROM User u WHERE u.email = :email")
   Optional<String> findRoleByEmail(@Param("email") String email);


}
