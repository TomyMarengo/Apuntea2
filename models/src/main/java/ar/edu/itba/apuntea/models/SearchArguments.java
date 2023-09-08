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
        // TODO: Add try catchs?
        if (institution != null && !institution.isEmpty()) this.institution = UUID.fromString(institution);
        if (career != null && !career.isEmpty()) this.career = UUID.fromString(career);
        if (subject != null && ! subject.isEmpty()) this.subject = UUID.fromString(subject);
        try {
            if (category != null) this.category = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.category = null;
        }
        this.score = score;
        try {
            if (sortBy != null) this.sortBy = SortBy.valueOf(sortBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.sortBy = SortBy.SCORE;
        }
        this.page = page;
        this.pageSize = pageSize;
    }

    public Optional<UUID> getInstitution() {
        return Optional.ofNullable(institution);
    }

    public Optional<UUID> getCareer() {
        return Optional.ofNullable(career);
    }

    public Optional<UUID> getSubject() {
        return Optional.ofNullable(subject);
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
