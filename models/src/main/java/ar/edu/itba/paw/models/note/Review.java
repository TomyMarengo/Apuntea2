package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reviews")
@IdClass(Review.ReviewId.class)
public class Review {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @Column(nullable = false)
    private int score;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "note_id")
    private Note note;

    @Column(name = "created_at", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    /* package-private */ Review() {}

    public Review(Note note, User user, String content, int score) {
        this.note = note;
        this.user = user;
        this.content = content;
        this.score = score;
        this.createdAt = LocalDateTime.now(); // TODO: See if it is possible to remove this
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

    public static class ReviewId implements Serializable {
        private User user;
        private Note note;

        /* package-private */ ReviewId() {}

        public ReviewId(User user, Note note) {
            this.user = user;
            this.note = note;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReviewId reviewId = (ReviewId) o;
            return Objects.equals(user, reviewId.user) && Objects.equals(note, reviewId.note);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, note);
        }
    }
}
