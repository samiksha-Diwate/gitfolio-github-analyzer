package gitfolio.controller;

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
import gitfolio.service.GitHubService;

@Controller
public class HomeController {

    private final GitHubService gitHubService;

    public HomeController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    // 🔥 IMPORTANT: redirect root to login (fixes your issue)
   
    public String root() {
        return "redirect:/login";
    }

    // 🔥 SEARCH PAGE (only after login)
    @GetMapping("/search")
    public String searchPage() {
        return "index";
    }

    // 🔥 MAIN SEARCH LOGIC
    @PostMapping("/search")
    public String search(@RequestParam String username,
                         Model model) {

        try {

            GitHubUser user = gitHubService.getUser(username);

            List<Repository> repositories =
                    gitHubService.getRepositories(username);

            repositories.sort(
                    Comparator.comparingInt(Repository::getStargazers_count).reversed()
            );

            int totalStars = 0;
            int totalForks = 0;

            Map<String, Integer> languageCount = new HashMap<>();

            for (Repository repo : repositories) {

                totalStars += repo.getStargazers_count();
                totalForks += repo.getForks_count();

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

        } catch (Exception e) {
            model.addAttribute("error", "GitHub user not found!");
        }

        return "index";
    }
}