package gitfolio.service;

import gitfolio.model.User;
import gitfolio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // REGISTER
    public User register(User user) {
        return userRepository.save(user);
    }

    // LOGIN
    public User login(String username, String password) {

        Optional<User> user =
                userRepository.findByUsernameAndPassword(username, password);

        return user.orElse(null);
    }
}