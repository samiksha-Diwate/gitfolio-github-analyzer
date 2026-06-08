# GitHub API Integration

## Endpoints Used
- GET /users/{username}: Fetch user metadata (avatar, name, followers).
- GET /users/{username}/repos: Fetch all public repositories of a user.

## Client Configuration
Uses Spring WebClient to non-blocking fetch from pi.github.com.
