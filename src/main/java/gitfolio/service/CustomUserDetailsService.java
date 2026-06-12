package gitfolio.service;

import gitfolio.model.User;
import gitfolio.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 🔥 DEBUG: login attempt
        System.out.println("🔥 LOGIN ATTEMPT: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("❌ USER NOT FOUND IN DB: " + username);
                    return new UsernameNotFoundException("User not found");
                });

        // ✅ DEBUG: user found
        System.out.println("✅ USER FOUND: " + user.getUsername());
        System.out.println("🔑 PASSWORD FROM DB: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}