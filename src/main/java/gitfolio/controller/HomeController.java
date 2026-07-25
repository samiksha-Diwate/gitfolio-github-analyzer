package gitfolio.controller;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import gitfolio.model.GitHubUser;
import gitfolio.model.Repository;
import gitfolio.model.User;
import gitfolio.repository.UserRepository;
import gitfolio.service.GitHubService;
import gitfolio.service.FavoriteProfileService;
import gitfolio.service.SearchHistoryService;
import gitfolio.exception.GitHubUserNotFoundException;
import gitfolio.exception.GitHubRateLimitExceededException;

@Controller
public class HomeController {

    private final GitHubService gitHubService;
    private final UserRepository userRepository;
    private final FavoriteProfileService favoriteProfileService;
    private final SearchHistoryService searchHistoryService;

    public HomeController(GitHubService gitHubService,
                          UserRepository userRepository,
                          FavoriteProfileService favoriteProfileService,
                          SearchHistoryService searchHistoryService) {
        this.gitHubService = gitHubService;
        this.userRepository = userRepository;
        this.favoriteProfileService = favoriteProfileService;
        this.searchHistoryService = searchHistoryService;
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam(required = false) String username,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", principal.getName());
        
        if (username != null && !username.trim().isEmpty()) {
            return performSearch(username.trim(), model, principal);
        }
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam String username,
                         Model model,
                         Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        return performSearch(username.trim(), model, principal);
    }

    private String performSearch(String username, Model model, Principal principal) {
        try {
            GitHubUser user = gitHubService.getUser(username);
            List<Repository> repositories = gitHubService.getRepositories(username);

            repositories.sort(
                    Comparator.comparingInt(Repository::getStargazers_count).reversed()
            );

            int totalStars = 0;
            int totalForks = 0;
            int totalOpenIssues = 0;

            Map<String, Integer> languageCount = new HashMap<>();

            for (Repository repo : repositories) {
                totalStars += repo.getStargazers_count();
                totalForks += repo.getForks_count();
                totalOpenIssues += repo.getOpen_issues_count();

                if (repo.getLanguage() != null) {
                    languageCount.put(
                            repo.getLanguage(),
                            languageCount.getOrDefault(repo.getLanguage(), 0) + 1
                    );
                }
            }

            // favorite language
            String favoriteLanguage = "None";
            int max = 0;

            for (Map.Entry<String, Integer> entry : languageCount.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    favoriteLanguage = entry.getKey();
                }
            }

            // most starred / forked repo
            String mostStarredRepo = "None";
            String mostForkedRepo = "None";
            int maxStars = 0;
            int maxForks = 0;

            for (Repository repo : repositories) {
                if (repo.getStargazers_count() > maxStars) {
                    maxStars = repo.getStargazers_count();
                    mostStarredRepo = repo.getName();
                }

                if (repo.getForks_count() > maxForks) {
                    maxForks = repo.getForks_count();
                    mostForkedRepo = repo.getName();
                }
            }

            double avgStars = repositories.isEmpty()
                    ? 0
                    : (double) totalStars / repositories.size();

            // Calculate Profile Score (0-100)
            int popularityScoreVal = Math.min(30, (totalStars + user.getFollowers()) * 2);
            int codingQtyScoreVal = Math.min(20, user.getPublic_repos() * 2);
            int collaborationScoreVal = Math.min(20, totalForks * 2);
            
            int completenessScoreVal = 0;
            if (user.getBio() != null && !user.getBio().trim().isEmpty()) completenessScoreVal += 2;
            if (user.getBlog() != null && !user.getBlog().trim().isEmpty()) completenessScoreVal += 2;
            if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) completenessScoreVal += 2;
            if (user.getCompany() != null && !user.getCompany().trim().isEmpty()) completenessScoreVal += 2;
            if (user.getLocation() != null && !user.getLocation().trim().isEmpty()) completenessScoreVal += 2;

            int activeRepos = 0;
            Instant ninetyDaysAgo = Instant.now().minus(90, ChronoUnit.DAYS);
            for (Repository repo : repositories) {
                if (repo.getUpdated_at() != null) {
                    try {
                        Instant updatedAt = Instant.parse(repo.getUpdated_at());
                        if (updatedAt.isAfter(ninetyDaysAgo)) {
                            activeRepos++;
                        }
                    } catch (Exception e) {
                        // ignore parse errors
                    }
                }
            }
            int activityScoreVal = Math.min(20, activeRepos * 4);
            
            int profileScore = popularityScoreVal + codingQtyScoreVal + collaborationScoreVal + completenessScoreVal + activityScoreVal;

            // Radar Chart dimensions (0-100 scale)
            int radarCoding = Math.min(100, user.getPublic_repos() * 4);
            int radarPopularity = Math.min(100, (totalStars + user.getFollowers()) * 3);
            int radarCollaboration = Math.min(100, totalForks * 5);
            int radarOpenness = Math.min(100, totalOpenIssues * 8);
            int radarCompleteness = completenessScoreVal * 10;

            // Track search history and check if favorite for current logged in user
            User loggedInUser = userRepository.findByUsername(principal.getName()).orElse(null);
            boolean isFavorite = false;
            if (loggedInUser != null) {
                searchHistoryService.addHistoryEntry(loggedInUser, user.getLogin());
                isFavorite = favoriteProfileService.isFavorite(loggedInUser, user.getLogin());
            }

            // send data to UI
            model.addAttribute("user", user);
            model.addAttribute("repositories", repositories);
            model.addAttribute("totalStars", totalStars);
            model.addAttribute("totalForks", totalForks);
            model.addAttribute("favoriteLanguage", favoriteLanguage);
            model.addAttribute("totalRepos", repositories.size());
            model.addAttribute("languageCount", languageCount.entrySet());
            model.addAttribute("mostStarredRepo", mostStarredRepo);
            model.addAttribute("mostForkedRepo", mostForkedRepo);
            model.addAttribute("totalLanguages", languageCount.size());
            model.addAttribute("avgStars", String.format("%.2f", avgStars));
            model.addAttribute("isFavorite", isFavorite);
            model.addAttribute("username", principal.getName());
            
            // Phase 2 Attributes
            model.addAttribute("profileScore", profileScore);
            model.addAttribute("radarCoding", radarCoding);
            model.addAttribute("radarPopularity", radarPopularity);
            model.addAttribute("radarCollaboration", radarCollaboration);
            model.addAttribute("radarOpenness", radarOpenness);
            model.addAttribute("radarCompleteness", radarCompleteness);

        } catch (GitHubUserNotFoundException e) {
            model.addAttribute("error", "GitHub user '" + username + "' not found!");
        } catch (GitHubRateLimitExceededException e) {
            model.addAttribute("error", "GitHub API rate limit exceeded! Please try again later or add an API token.");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to retrieve profile: " + e.getMessage());
        }

        return "index";
    }
}