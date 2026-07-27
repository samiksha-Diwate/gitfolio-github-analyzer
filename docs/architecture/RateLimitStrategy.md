# GitHub API Rate Limit Mitigation

## Rate Limit Headers
GitHub API responses contain the following headers:
- X-RateLimit-Limit
- X-RateLimit-Remaining
- X-RateLimit-Reset

We monitor and cache search results to avoid hitting rate limits.
