package ar.edu.itba.paw.webapp.controller.review.dtos;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ReviewUpdateDto {

    @Size(max = 255)
    private String content;

    @Range(min = 1, max = 5)
    private Integer score;

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
