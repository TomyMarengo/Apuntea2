package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.note.Review;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewDto {
    private UUID userId;
    private UUID noteId;
    private String content;
    private LocalDateTime createdAt;
    private int score;
    private URI self;
    private URI note;
    private URI user;

    public ReviewDto() {
    }

    public static ReviewDto fromReview(final Review review, final UriInfo uriInfo) {
        final ReviewDto dto = new ReviewDto();
        dto.userId = review.getUser().getUserId();
        dto.noteId = review.getNote().getId();
        dto.content = review.getContent();
        dto.createdAt = review.getCreatedAt();
        dto.score = review.getScore();
        dto.self = uriInfo.getBaseUriBuilder().path("reviews").path(review.getNote().getId().toString() + "_" +review.getUser().getUserId().toString()).build();
        dto.note = uriInfo.getBaseUriBuilder().path("notes").path(review.getNote().getId().toString()).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(review.getUser().getUserId().toString()).build();
        return dto;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getNote() {
        return note;
    }

    public void setNote(URI note) {
        this.note = note;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }


}
