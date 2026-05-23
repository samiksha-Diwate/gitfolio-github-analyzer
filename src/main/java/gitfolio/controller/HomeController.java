package gitfolio.controller;

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

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam String username,
                         Model model) {

        try {

            GitHubUser user =
                    gitHubService.getUser(username);

            List<Repository> repositories =
                    gitHubService.getRepositories(username);

            int totalStars = 0;
            int totalForks = 0;

            Map<String, Integer> languageCount =
                    new HashMap<>();

            for (Repository repo : repositories) {

                totalStars += repo.getStargazers_count();

                totalForks += repo.getForks_count();

                String language = repo.getLanguage();

                if (language != null) {

                    languageCount.put(
                            language,

                            languageCount.getOrDefault(
                                    language, 0) + 1
                    );
                }
            }

            String topLanguage = "Not Available";

            int max = 0;

            for (String lang : languageCount.keySet()) {

                if (languageCount.get(lang) > max) {

                    max = languageCount.get(lang);

                    topLanguage = lang;
                }
            }

            model.addAttribute("user", user);

            model.addAttribute("repositories",
                    repositories);

            model.addAttribute("totalStars",
                    totalStars);

            model.addAttribute("totalForks",
                    totalForks);

            model.addAttribute("topLanguage",
                    topLanguage);

            model.addAttribute("repoCount",
                    repositories.size());

        }

        catch (Exception e) {

            model.addAttribute("error",
                    "GitHub user not found!");
        }

        return "index";
    }
}