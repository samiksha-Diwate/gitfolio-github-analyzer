package gitfolio.controller;

import gitfolio.model.User;
import gitfolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

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
                          @RequestParam String password) {

        User user = userService.login(username, password);

        if (user != null) {
            return "redirect:/search-page";
        } else {
            return "redirect:/home";
        }
    }
}