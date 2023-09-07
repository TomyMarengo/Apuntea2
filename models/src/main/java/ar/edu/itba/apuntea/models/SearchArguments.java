package ar.edu.itba.apuntea.models;

import java.util.Optional;
import java.util.UUID;

public class SearchArguments {
    private UUID institution;
    private UUID career;
    private UUID subject;
    private Category category; //TODO: check database type
    private Float score;
    private SortBy sortBy;
    private Integer page;
    private Integer pageSize;

    public SearchArguments(String institution, String career, String subject, String category, Float score, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        this.institution = UUID.fromString(institution);
        this.career = UUID.fromString(career);
        this.subject = UUID.fromString(subject);
        try {
            this.category = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.category = null;
        }
        this.score = score;

        try {
            this.sortBy = SortBy.valueOf(sortBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.sortBy = SortBy.SCORE;
        }
        this.page = page;
        this.pageSize = pageSize;
    }

    public UUID getInstitution() {
        return institution;
    }

    public UUID getCareer() {
        return career;
    }

    public UUID getSubject() {
        return subject;
    }

    public Optional<Category> getCategory() {
        return Optional.ofNullable(category);
    }

    public Optional<Float> getScore() {
        return Optional.ofNullable(score);
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public enum SortBy {
        NAME,SCORE,DATE;
    }
}
