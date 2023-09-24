package ar.edu.itba.paw.models;

import java.util.Optional;
import java.util.UUID;

public class SearchArguments {
    private UUID institutionId;
    private UUID careerId;
    private UUID subjectId;
    private Category category;
    private String word;
    private UUID userId;
    private UUID currentUserId;
    private SortBy sortBy;
    private boolean ascending;
    private int page;
    private int pageSize;

    public SearchArguments() {
        this.sortBy = SortBy.NAME;
        this.ascending = true;
        this.page = 1;
        this.pageSize = 10;
    }

    public SearchArguments withInstitutionId(UUID institutionId) {
        this.institutionId = institutionId;
        return this;
    }

    public SearchArguments withCareerId(UUID careerId) {
        this.careerId = careerId;
        return this;
    }

    public SearchArguments withSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public SearchArguments withCategory(String category) {
        if (category != null && !category.isEmpty())
            this.category = Category.valueOf(category.toUpperCase());
        return this;
    }

    public SearchArguments withWord(String word) {
        this.word = word;
        return this;
    }

    public SearchArguments withUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public SearchArguments withCurrentUserId(UUID currentUserId) {
        this.currentUserId = currentUserId;
        return this;
    }

    public SearchArguments withSortBy(String sortBy) {
        if (sortBy != null && !sortBy.isEmpty())
            this.sortBy = SortBy.valueOf(sortBy.toUpperCase());
        return this;
    }

    public SearchArguments withAscending(boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    public SearchArguments withPage(Integer page) {
        this.page = page;
        return this;
    }

    public SearchArguments withPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
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

    public Optional<UUID> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Optional<UUID> getCurrentUserId() {
        return Optional.ofNullable(currentUserId);
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public boolean isAscending() { return ascending; }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public enum SortBy {
        NAME, SCORE, DATE;
    }
}
