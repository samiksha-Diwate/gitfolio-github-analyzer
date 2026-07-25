package gitfolio.controller;

import gitfolio.model.User;
import gitfolio.model.FavoriteProfile;
import gitfolio.repository.UserRepository;
import gitfolio.service.FavoriteProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class FavoriteController {

    private final UserRepository userRepository;
    private final FavoriteProfileService favoriteProfileService;

    public FavoriteController(UserRepository userRepository, FavoriteProfileService favoriteProfileService) {
        this.userRepository = userRepository;
        this.favoriteProfileService = favoriteProfileService;
    }

    @GetMapping("/favorites")
    public String viewFavorites(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            List<FavoriteProfile> favorites = favoriteProfileService.getFavoritesForUser(user);
            model.addAttribute("favorites", favorites);
            model.addAttribute("username", user.getUsername());
        }
        return "favorites";
    }

    @PostMapping("/favorites/add")
    public String addFavorite(@RequestParam String username,
                              @RequestParam(required = false) String avatarUrl,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String bio,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            favoriteProfileService.addFavorite(user, username, avatarUrl, name, bio);
            redirectAttributes.addFlashAttribute("message", "Profile saved to favorites!");
        }
        return "redirect:/search?username=" + username;
    }

    @PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam String username,
                                 @RequestParam(required = false) String redirectUrl,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            favoriteProfileService.removeFavorite(user, username);
            redirectAttributes.addFlashAttribute("message", "Profile removed from favorites.");
        }
        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/favorites";
    }
}
