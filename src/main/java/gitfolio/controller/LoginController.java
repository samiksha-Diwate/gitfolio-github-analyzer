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

        System.out.println("Username = " + username);
        System.out.println("Password = " + password);
        System.out.println("User Found = " + user);

        if (user != null) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/login?error";
        }
    }

    // TEMP PAGE TO TEST LOGIN SUCCESS
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}