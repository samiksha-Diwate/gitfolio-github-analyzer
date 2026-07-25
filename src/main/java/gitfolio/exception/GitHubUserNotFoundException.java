package gitfolio.exception;

public class GitHubUserNotFoundException extends GitHubApiException {
    public GitHubUserNotFoundException(String username) {
        super("GitHub username '" + username + "' not found!");
    }
}
