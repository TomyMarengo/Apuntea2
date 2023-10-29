package ar.edu.itba.paw.models.institutional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "subjects_careers")
@IdClass(SubjectCareer.SubjectCareerKey.class)
public class SubjectCareer {
    private static final int MIN_YEAR = 1;
    private static final int MAX_YEAR = 10;

    @Id
    @ManyToOne
    @JoinColumn(name = "subject_id")
    @MapsId
    private Subject subject;

    @Id
    @ManyToOne
    @JoinColumn(name = "career_id")
    @MapsId
    private Career career;

    private int year;

    /* package-private */ SubjectCareer() {

    }

    public SubjectCareer(Subject subject, Career career, int year) {
        this.subject = subject;
        this.career = career;
        this.year = year;
    }

    public Subject getSubject() {
        return subject;
    }
    public Career getCareer() {
        return career;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < MIN_YEAR || year > MAX_YEAR)
            throw new IllegalArgumentException();
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectCareer that = (SubjectCareer) o;
        return Objects.equals(subject, that.subject) && Objects.equals(career, that.career);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, career);
    }

    public static class SubjectCareerKey implements Serializable {
        private Subject subject;
        private Career career;

        /* package-private */ SubjectCareerKey() {}

        public SubjectCareerKey(Subject subject, Career career) {
            this.subject = subject;
            this.career = career;
        }

        public Subject getSubject() {
            return subject;
        }

        public Career getCareer() {
            return career;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SubjectCareerKey)) return false;
            SubjectCareerKey that = (SubjectCareerKey) o;
            return Objects.equals(subject, that.subject) && Objects.equals(career, that.career);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subject, career);
        }
    }
}
