package gitfolio.controller;

import gitfolio.service.UserService;
import gitfolio.model.User;
import gitfolio.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/do-login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session) {

        User user = userService.login(username, password);

        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/dashboard";
        }

        return "redirect:/login?error";
    }

    @Bean
CommandLineRunner run(UserRepository repo, PasswordEncoder encoder) {
    return args -> {
        repo.save(new User(
            "testuser",
            encoder.encode("test123")
        ));
    };
}

   @GetMapping("/dashboard")
public String dashboard(Model model, Principal principal) {

    String username = principal.getName();

    model.addAttribute("username", username);

    return "dashboard";
}
}