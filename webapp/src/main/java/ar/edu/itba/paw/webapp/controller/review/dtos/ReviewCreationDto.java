package ar.edu.itba.paw.webapp.controller.review.dtos;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;
import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.UUID;

public class ReviewCreationDto {
    @ValidUuid
    private UUID noteId;

    @Size(max = 255)
    private String content;

    @Min(1)
    @Max(5)
    private int score;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }
}
