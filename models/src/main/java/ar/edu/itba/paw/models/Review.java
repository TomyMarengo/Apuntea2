package ar.edu.itba.paw.models;

public class Review {
    private final User user;
    private final String content;
    private final Integer score;

    public Review(User user, String comment, Integer score) {
        this.user = user;
        this.content = comment;
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Integer getScore() {
        return score;
    }
}
