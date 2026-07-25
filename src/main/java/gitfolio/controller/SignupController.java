package gitfolio.controller;

import gitfolio.model.User;
import gitfolio.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class SignupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String signupPage(Principal principal) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Username cannot be empty");
            return "signup";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Password cannot be empty");
            return "signup";
        }
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long");
            return "signup";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }
        if (userRepository.findByUsername(username.trim()).isPresent()) {
            model.addAttribute("error", "Username is already taken");
            return "signup";
        }

        User user = new User(username.trim(), passwordEncoder.encode(password));
        userRepository.save(user);

        return "redirect:/login?signup_success";
    }
}
