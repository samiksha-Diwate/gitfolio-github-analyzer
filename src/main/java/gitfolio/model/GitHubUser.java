package gitfolio.model;

import lombok.Data;

@Data
public class GitHubUser {

    private String login;

    private String name;

    private String avatar_url;

    private String bio;

    private int followers;

    private int following;

    private int public_repos;
}