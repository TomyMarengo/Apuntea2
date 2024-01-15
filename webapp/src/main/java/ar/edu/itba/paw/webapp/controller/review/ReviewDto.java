package ar.edu.itba.paw.webapp.controller.review;

import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class ReviewDto {

    @Size(max = 255)
    private String content;

    @Min(1)
    @Max(5)
    private int score;

    public ReviewDto() {
    }

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
}
