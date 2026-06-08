package gitfolio.repository;

import gitfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // FIXED: matches your User entity (username + password)
    Optional<User> findByUsernameAndPassword(String username, String password);
}