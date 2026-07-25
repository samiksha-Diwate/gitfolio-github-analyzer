package gitfolio.service;

import gitfolio.model.SearchHistory;
import gitfolio.model.User;
import gitfolio.repository.SearchHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public List<SearchHistory> getHistoryForUser(User user) {
        return searchHistoryRepository.findByUserOrderBySearchedAtDesc(user);
    }

    public void addHistoryEntry(User user, String githubUsername) {
        String trimmedUsername = githubUsername.trim();
        Optional<SearchHistory> existing = searchHistoryRepository.findByUserAndGithubUsernameIgnoreCase(user, trimmedUsername);
        if (existing.isPresent()) {
            SearchHistory history = existing.get();
            history.setSearchedAt(Instant.now());
            searchHistoryRepository.save(history);
        } else {
            SearchHistory history = new SearchHistory(user, trimmedUsername);
            searchHistoryRepository.save(history);
        }
    }

    public void removeHistoryEntry(User user, String githubUsername) {
        searchHistoryRepository.deleteByUserAndGithubUsernameIgnoreCase(user, githubUsername);
    }

    public void clearHistory(User user) {
        searchHistoryRepository.deleteByUser(user);
    }
}
