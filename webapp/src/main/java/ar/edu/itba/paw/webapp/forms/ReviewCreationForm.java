package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.util.UUID;

public class ReviewCreationForm {
    private UUID noteId;

    @Size(max = 255, message = "{error.param.length}")
    private String content;

    @Range(min = 1, max = 5, message = "{error.param.range}")
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
