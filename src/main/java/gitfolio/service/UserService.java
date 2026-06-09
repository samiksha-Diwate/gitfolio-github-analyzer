package gitfolio.service;

import gitfolio.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User login(String username, String password) {

        if ("admin".equals(username) && "1234".equals(password)) {
            return new User(username, password);
        }

        return null;
    }

    public User register(User user) {
        return user;
    }
}