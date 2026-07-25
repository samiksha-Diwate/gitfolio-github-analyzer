package gitfolio.controller;

import gitfolio.model.User;
import gitfolio.model.FavoriteProfile;
import gitfolio.model.SearchHistory;
import gitfolio.repository.UserRepository;
import gitfolio.service.FavoriteProfileService;
import gitfolio.service.SearchHistoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final FavoriteProfileService favoriteProfileService;
    private final SearchHistoryService searchHistoryService;

    public LoginController(UserRepository userRepository,
                           FavoriteProfileService favoriteProfileService,
                           SearchHistoryService searchHistoryService) {
        this.userRepository = userRepository;
        this.favoriteProfileService = favoriteProfileService;
        this.searchHistoryService = searchHistoryService;
    }

    @GetMapping("/")
    public String home(Principal principal) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Principal principal) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @Bean
    CommandLineRunner run(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("testuser").isEmpty()) {
                repo.save(new User(
                    "testuser",
                    encoder.encode("test123")
                ));
            }
        };
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            List<FavoriteProfile> favorites = favoriteProfileService.getFavoritesForUser(user);
            List<SearchHistory> history = searchHistoryService.getHistoryForUser(user);
            
            // Slice lists to top 5 for dashboard summary
            List<FavoriteProfile> recentFavorites = favorites.stream().limit(5).toList();
            List<SearchHistory> recentHistory = history.stream().limit(5).toList();

            model.addAttribute("username", username);
            model.addAttribute("favorites", recentFavorites);
            model.addAttribute("history", recentHistory);
            model.addAttribute("favoritesCount", favorites.size());
            model.addAttribute("historyCount", history.size());
        }
        return "dashboard";
    }
}