package ar.edu.itba.paw.webapp.controller.subject.dtos;

import org.hibernate.validator.constraints.Range;

public class SubjectCareerUpdateDto {
    @Range(min = 1, max = 10, message = "{error.param.range}")
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}