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

            Repository topRepo = null;

            Map<String,Integer> languageCount =
                    new HashMap<>();

            for(Repository repo : repositories){

                totalStars +=
                        repo.getStargazers_count();

                totalForks +=
                        repo.getForks_count();

                if(topRepo == null ||
                   repo.getStargazers_count() >
                   topRepo.getStargazers_count()){

                    topRepo = repo;
                }

                if(repo.getLanguage() != null){

                    languageCount.put(

                            repo.getLanguage(),

                            languageCount.getOrDefault(
                                    repo.getLanguage(),
                                    0
                            ) + 1
                    );
                }
            }

            String topLanguage = "None";

            int max = 0;

            for(String lang : languageCount.keySet()){

                if(languageCount.get(lang) > max){

                    max = languageCount.get(lang);

                    topLanguage = lang;
                }
            }

            model.addAttribute("user", user);

            model.addAttribute("repositories",
                    repositories);

            model.addAttribute("repoCount",
                    repositories.size());

            model.addAttribute("totalStars",
                    totalStars);

            model.addAttribute("totalForks",
                    totalForks);

            model.addAttribute("topRepo",
                    topRepo);

            model.addAttribute("topLanguage",
                    topLanguage);

            model.addAttribute("languageData",
                    languageCount);

        }

        catch (Exception e){

            model.addAttribute("error",
                    "GitHub user not found!");
        }

        return "index";
    }
}