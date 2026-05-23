package gitfolio.controller;

import java.util.List;

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

        model.addAttribute("user", user);

        model.addAttribute("repositories",
                repositories);

    }

    catch (Exception e) {

        model.addAttribute("error",
                "GitHub user not found!");
    }

    return "index";
}
}