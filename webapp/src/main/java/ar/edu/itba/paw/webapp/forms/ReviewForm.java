package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

public class ReviewForm {
    @Range(min = 1, max = 5)
    private Integer score;

    @Length(max = 255)
    private String content;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String text) {
        this.content = text;
    }
}
