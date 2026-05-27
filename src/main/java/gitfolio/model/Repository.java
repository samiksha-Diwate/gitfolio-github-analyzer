package gitfolio.model;

public class Repository {

    private String name;
    private String html_url;
    private String description;
    private int stargazers_count;
    private int forks_count;
    private String language;

    // GETTERS

    public String getName() {
        return name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getDescription() {
        return description;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public int getForks_count() {
        return forks_count;
    }

    public String getLanguage() {
        return language;
    }

    // SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}