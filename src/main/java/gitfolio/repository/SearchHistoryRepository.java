package gitfolio.repository;

import gitfolio.model.SearchHistory;
import gitfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user);
    Optional<SearchHistory> findByUserAndGithubUsernameIgnoreCase(User user, String githubUsername);
    void deleteByUserAndGithubUsernameIgnoreCase(User user, String githubUsername);
    void deleteByUser(User user);
}
