package gitfolio.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import gitfolio.model.GitHubUser;
import gitfolio.model.Repository;

@Service
public class GitHubService {

    private final WebClient webClient;

    public GitHubService(WebClient.Builder builder) {

        this.webClient = builder
                .baseUrl("https://api.github.com")
                .build();
    }

    public GitHubUser getUser(String username) {

        return webClient
                .get()
                .uri("/users/" + username)
                .retrieve()
                .bodyToMono(GitHubUser.class)
                .block();
    }

    public List<Repository> getRepositories(String username) {

        return webClient
                .get()
                .uri("/users/" + username + "/repos")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Repository>>() {})
                .block();
    }
}