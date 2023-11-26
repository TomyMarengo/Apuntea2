package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@IdClass(Review.ReviewKey.class)
public class Review {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private User user;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    private String content;

    @Column(nullable = false)
    private int score;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", insertable = false, updatable = false, nullable = false)
    private Note note;

    @Id
    @Column(name = "note_id")
    private UUID noteId;

    @Column(name = "created_at", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    /* package-private */ Review() {}

    public Review(Note note, User user, int score, String content) {
        this.note = note;
        this.user = user;
        this.userId = user.getUserId();
        this.noteId = note.getId();
        this.content = content;
        this.score = score;
    }

    public Review(UUID noteId, UUID userId, int score, String content) {
        this.noteId = noteId;
        this.userId = userId;
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

    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public static class ReviewKey implements Serializable {
        private UUID userId;
        private UUID noteId;

        /* package-private */ ReviewKey() {}

        public ReviewKey(UUID userId, UUID noteId) {
            this.userId = userId;
            this.noteId = noteId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReviewKey reviewId = (ReviewKey) o;
            return Objects.equals(userId, reviewId.userId) && Objects.equals(noteId, reviewId.noteId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, noteId);
        }
    }
}
