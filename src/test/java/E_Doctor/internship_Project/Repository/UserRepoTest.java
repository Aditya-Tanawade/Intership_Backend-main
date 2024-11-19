package E_Doctor.internship_Project.Repository;

import E_Doctor.internship_Project.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use H2 database
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole("USER");
        userRepo.save(user);
    }

    @Test
    void testFindByUsername() {
        Optional<User> foundUser = userRepo.findByUsername("testuser");

        assertTrue(foundUser.isPresent(), "User with username 'testuser' should be found");
        assertEquals("test@example.com", foundUser.get().getEmail(), "Email should match the expected value");
    }

    @Test
    void testFindByEmail() {
        Optional<User> foundUser = userRepo.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent(), "User with email 'test@example.com' should be found");
        assertEquals("testuser", foundUser.get().getUsername(), "Username should match the expected value");
    }

    @Test
    void testFindRoleByEmail() {
        Optional<String> role = userRepo.findRoleByEmail("test@example.com");

        assertTrue(role.isPresent(), "Role for email 'test@example.com' should be found");
        assertEquals("USER", role.get(), "Role should match the expected value");
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> foundUser = userRepo.findByUsername("nonexistentuser");

        assertFalse(foundUser.isPresent(), "No user should be found for a nonexistent username");
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> foundUser = userRepo.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent(), "No user should be found for a nonexistent email");
    }

    @Test
    void testFindRoleByEmail_NotFound() {
        Optional<String> role = userRepo.findRoleByEmail("nonexistent@example.com");

        assertFalse(role.isPresent(), "No role should be found for a nonexistent email");
    }
}

