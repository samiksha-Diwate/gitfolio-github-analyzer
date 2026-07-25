package gitfolio.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "favorite_profiles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "github_username"})
})
public class FavoriteProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "github_username", nullable = false)
    private String githubUsername;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public FavoriteProfile() {
    }

    public FavoriteProfile(User user, String githubUsername, String avatarUrl, String name, String bio) {
        this.user = user;
        this.githubUsername = githubUsername;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
