package ar.edu.itba.paw.models;

public class Review {
    private final User user;
    private final String content;
    private final Integer score;
    private Note note;

    public Review(User user, String comment, Integer score) {
        this.user = user;
        this.content = comment;
        this.score = score;
    }

    public Review(User user, String content, Integer score, Note note) {
        this(user, content, score);
        this.note = note;
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

    public Note getNote() {
        return note;
    }
}
