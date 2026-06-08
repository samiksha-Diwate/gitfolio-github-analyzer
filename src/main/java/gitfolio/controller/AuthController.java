package gitfolio.controller;

import gitfolio.model.User;
import gitfolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userService.register(user);

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        User user = userService.login(username, password);

        if (user != null) {
            return "redirect:/search";
        } else {
            return "redirect:/login?error";
        }
    }
}