package ar.edu.itba.paw.models;

import java.util.Optional;
import java.util.UUID;

public class SearchArguments {
    private UUID institutionId;
    private UUID careerId;
    private UUID subjectId;
    private Category category; //TODO: check database type
    private String word;
    private SortBy sortBy;
    private boolean ascending;
    private Integer page;
    private Integer pageSize;


     public SearchArguments(UUID institutionId, UUID careerId, UUID subjectId, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        this(institutionId, careerId, subjectId, null, word, sortBy, ascending, page, pageSize);
    }
    
    public SearchArguments(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        this.institutionId = institutionId;
        this.careerId = careerId;
        this.subjectId = subjectId;
        if (category != null && !category.isEmpty())
            this.category = Category.valueOf(category.toUpperCase());
        if (sortBy != null && !sortBy.isEmpty())
            this.sortBy = SortBy.valueOf(sortBy.toUpperCase());
        this.word = word;
        this.ascending = ascending;
        this.page = page;
        this.pageSize = pageSize;
    }

    public Optional<UUID> getInstitutionId() {
        return Optional.ofNullable(institutionId);
    }

    public Optional<UUID> getCareerId() {
        return Optional.ofNullable(careerId);
    }

    public Optional<UUID> getSubjectId() {
        return Optional.ofNullable(subjectId);
    }

    public Optional<Category> getCategory() {
        return Optional.ofNullable(category);
    }

    public Optional<String> getWord() {
        return Optional.ofNullable(word);
    }
    public SortBy getSortBy() {
        return sortBy;
    }
    public boolean isAscending() { return ascending; }
    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public enum SortBy {
        NAME, SCORE, DATE;
    }
}
