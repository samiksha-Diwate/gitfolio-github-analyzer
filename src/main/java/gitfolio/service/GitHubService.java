package gitfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import gitfolio.model.GitHubUser;
import gitfolio.model.Repository;
import gitfolio.exception.GitHubApiException;
import gitfolio.exception.GitHubRateLimitExceededException;
import gitfolio.exception.GitHubUserNotFoundException;

@Service
public class GitHubService {

    private final WebClient webClient;

    public GitHubService(WebClient.Builder builder, @Value("${github.token:}") String githubToken) {
        WebClient.Builder webClientBuilder = builder.baseUrl("https://api.github.com");
        
        if (githubToken != null && !githubToken.trim().isEmpty()) {
            webClientBuilder.defaultHeader("Authorization", "token " + githubToken.trim());
        }
        
        this.webClient = webClientBuilder.build();
    }

    public GitHubUser getUser(String username) {
        try {
            return webClient
                    .get()
                    .uri("/users/" + username)
                    .retrieve()
                    .bodyToMono(GitHubUser.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new GitHubUserNotFoundException(username);
        } catch (WebClientResponseException.Forbidden ex) {
            throw new GitHubRateLimitExceededException();
        } catch (Exception ex) {
            throw new GitHubApiException("Failed to fetch user profile: " + ex.getMessage(), ex);
        }
    }

    public List<Repository> getRepositories(String username) {
        try {
            return webClient
                    .get()
                    .uri("/users/" + username + "/repos")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Repository>>() {})
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new GitHubUserNotFoundException(username);
        } catch (WebClientResponseException.Forbidden ex) {
            throw new GitHubRateLimitExceededException();
        } catch (Exception ex) {
            throw new GitHubApiException("Failed to fetch repositories: " + ex.getMessage(), ex);
        }
    }
}