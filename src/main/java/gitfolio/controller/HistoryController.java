package gitfolio.controller;

import gitfolio.model.User;
import gitfolio.model.SearchHistory;
import gitfolio.repository.UserRepository;
import gitfolio.service.SearchHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class HistoryController {

    private final UserRepository userRepository;
    private final SearchHistoryService searchHistoryService;

    public HistoryController(UserRepository userRepository, SearchHistoryService searchHistoryService) {
        this.userRepository = userRepository;
        this.searchHistoryService = searchHistoryService;
    }

    @GetMapping("/history")
    public String viewHistory(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            List<SearchHistory> history = searchHistoryService.getHistoryForUser(user);
            model.addAttribute("history", history);
            model.addAttribute("username", user.getUsername());
        }
        return "history";
    }

    @PostMapping("/history/delete")
    public String deleteHistoryEntry(@RequestParam String username, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            searchHistoryService.removeHistoryEntry(user, username);
        }
        return "redirect:/history";
    }

    @PostMapping("/history/clear")
    public String clearHistory(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            searchHistoryService.clearHistory(user);
        }
        return "redirect:/history";
    }
}
