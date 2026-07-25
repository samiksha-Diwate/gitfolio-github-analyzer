package gitfolio.repository;

import gitfolio.model.FavoriteProfile;
import gitfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProfileRepository extends JpaRepository<FavoriteProfile, Long> {
    List<FavoriteProfile> findByUserOrderByCreatedAtDesc(User user);
    Optional<FavoriteProfile> findByUserAndGithubUsernameIgnoreCase(User user, String githubUsername);
    void deleteByUserAndGithubUsernameIgnoreCase(User user, String githubUsername);
    boolean existsByUserAndGithubUsernameIgnoreCase(User user, String githubUsername);
}
