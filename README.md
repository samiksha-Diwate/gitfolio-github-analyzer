# GitFolio 🚀

GitFolio is a premium, developer-centric GitHub Profile Analyzer built with Spring Boot, Thymeleaf, Spring Data JPA, H2, and Chart.js. It features a complete user authentication flow, live API integration, saved favorites, search logs, and interactive analytics.

## Features

- **User Authentication**: Secure signup, login, and logout flow using Spring Security and BCrypt password hashing.
- **GitHub API Integration**: Dynamic profile lookup handling rate-limiting and invalid usernames gracefully, with optional Personal Access Token (PAT) authentication.
- **Interactive Analytics Dashboard**: Beautiful visualizations using Chart.js depicting language distribution and repository stars.
- **Repository Section**: Detail views featuring stars, forks, open issues, size, language, and clickable repository redirect links.
- **Repository Search & Sort**: Fast client-side repository search and sort by stars, forks, and name.
- **Favorite Profiles**: Logged-in users can save their favorite profiles to look up later or remove them from favorites.
- **Search History**: A search log of previously analyzed profiles with revisit and single/all deletion support.
- **UI/UX Design**: Modern dark theme layout featuring responsive sidebar navigation, stats summary widgets, quick action dashboard tables, and custom alerts.

## Tech Stack

- **Backend**: Spring Boot 3.3.6 (Java 21), Spring Security, Spring Data JPA
- **Database**: H2 Database (in-memory, local testing)
- **Frontend**: HTML5, Thymeleaf, Bootstrap 5, Bootstrap Icons, CSS3 (Vanilla), JavaScript (ES6+)
- **API Call Client**: Spring Boot WebFlux (WebClient)
- **Charts**: Chart.js

## Getting Started

### Prerequisites

- Java 21+
- Maven (or use Maven Wrapper `.\mvnw.cmd` / `./mvnw`)

### Configuration

You can configure an optional GitHub Access Token in `src/main/resources/application.properties` to increase the API rate limit (60 to 5000 requests/hr):

```properties
github.token=your_personal_access_token_here
```

### Run Locally

1. Clone or copy the project.
2. Run the application from the root directory:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
3. Open your browser and navigate to `http://localhost:9091`.

### Demo Account

A test account is automatically seeded on startup for instant access:
- **Username**: `testuser`
- **Password**: `test123`