package gitfolio.controller;

import gitfolio.model.User;
import gitfolio.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        // ✅ Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user in database
        userRepository.save(user);

        return "User registered successfully!";
    }
}