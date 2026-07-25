package gitfolio.exception;

public class GitHubRateLimitExceededException extends GitHubApiException {
    public GitHubRateLimitExceededException() {
        super("GitHub API rate limit exceeded! Please try again later or add an API token.");
    }
}
