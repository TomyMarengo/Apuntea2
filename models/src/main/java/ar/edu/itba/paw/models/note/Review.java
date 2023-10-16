package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.user.User;

import java.time.LocalDateTime;

public class Review {
    private final User user;
    private final String content;
    private final int score;
    private Note note;
    private LocalDateTime createdAt;

    public Review(User user, String content, int score, LocalDateTime createdAt) {
        this.user = user;
        this.content = content;
        this.score = score;
        this.createdAt = createdAt;
    }

    public Review(User user, String content, int score, Note note) {
        this.user = user;
        this.content = content;
        this.score = score;
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public Note getNote() {
        return note;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
}
