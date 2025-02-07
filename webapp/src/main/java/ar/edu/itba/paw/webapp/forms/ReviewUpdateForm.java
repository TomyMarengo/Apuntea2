package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;

public class ReviewUpdateForm {
    @Size(max = 255, message = "{error.param.length}")
    private String content;

    @Range(min = 1, max = 5, message = "{error.param.range}")
    private Integer score;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
