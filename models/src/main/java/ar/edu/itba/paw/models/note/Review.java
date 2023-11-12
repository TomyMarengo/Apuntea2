package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reviews")
@IdClass(Review.ReviewKey.class)
public class Review {
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @Column(nullable = false)
    private int score;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "note_id")
    private Note note;

    @Column(name = "created_at", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    /* package-private */ Review() {}

    public Review(Note note, User user, int score, String content) {
        this.note = note;
        this.user = user;
        this.content = content;
        this.score = score;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        createdAt = LocalDateTime.now();
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

    public static class ReviewKey implements Serializable {
        private User user;
        private Note note;

        /* package-private */ ReviewKey() {}

        public ReviewKey(User user, Note note) {
            this.user = user;
            this.note = note;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReviewKey reviewId = (ReviewKey) o;
            return Objects.equals(user, reviewId.user) && Objects.equals(note, reviewId.note);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, note);
        }
    }
}
