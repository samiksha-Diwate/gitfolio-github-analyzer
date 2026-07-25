package gitfolio.service;

import gitfolio.model.FavoriteProfile;
import gitfolio.model.User;
import gitfolio.repository.FavoriteProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteProfileService {

    private final FavoriteProfileRepository favoriteProfileRepository;

    public FavoriteProfileService(FavoriteProfileRepository favoriteProfileRepository) {
        this.favoriteProfileRepository = favoriteProfileRepository;
    }

    public List<FavoriteProfile> getFavoritesForUser(User user) {
        return favoriteProfileRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public void addFavorite(User user, String githubUsername, String avatarUrl, String name, String bio) {
        if (!favoriteProfileRepository.existsByUserAndGithubUsernameIgnoreCase(user, githubUsername)) {
            FavoriteProfile favorite = new FavoriteProfile(user, githubUsername.trim(), avatarUrl, name, bio);
            favoriteProfileRepository.save(favorite);
        }
    }

    public void removeFavorite(User user, String githubUsername) {
        favoriteProfileRepository.deleteByUserAndGithubUsernameIgnoreCase(user, githubUsername);
    }

    public boolean isFavorite(User user, String githubUsername) {
        return favoriteProfileRepository.existsByUserAndGithubUsernameIgnoreCase(user, githubUsername);
    }
}
