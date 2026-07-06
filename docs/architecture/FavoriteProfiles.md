# Favorite Profiles CRUD Service

## Operations
- **Save Favorite:** POST /favorites/add?username=abc
- **List Favorites:** GET /favorites
- **Delete Favorite:** DELETE /favorites/remove/{id}
"@

Commit-Backdated -RepoPath C:\Users\ACER\Projects\gitfolio-github-analyzer -RelativeFilePath "docs/architecture/ThymeleafTemplates.md" -Date "2026-07-13 13:50:00" -Message "docs: explain Thymeleaf layout dialect and dynamic dashboard fragments" -Content @"
# Thymeleaf Front-end Layout

## Layout Patterns
Using 	hymeleaf-layout-dialect to inherit the navbar and sidebar base structure in all pages (dashboard.html, history.html, avorites.html).
Dynamic model variables inject profile search results.
