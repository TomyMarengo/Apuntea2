package ar.edu.itba.paw.models.search;

import ar.edu.itba.paw.models.Category;

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
    private SortArguments sortArgs;
    private int page;
    private int pageSize;

    private SearchArguments(SearchArguments.SearchArgumentsBuilder builder) {
        this.institutionId = builder.institutionId;
        this.careerId = builder.careerId;
        this.subjectId = builder.subjectId;
        this.category = builder.category;
        this.word = builder.word;
        this.userId = builder.userId;
        this.currentUserId = builder.currentUserId;
        this.sortArgs = builder.sortArgs;
        this.page = builder.page;
        this.pageSize = builder.pageSize;
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

    public SortArguments getSortArguments() { return sortArgs; }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public static class SearchArgumentsBuilder {
        private UUID institutionId;
        private UUID careerId;
        private UUID subjectId;
        private Category category;
        private String word;
        private UUID userId;
        private UUID currentUserId;
        private SortArguments sortArgs;
        private int page;
        private int pageSize;

        public SearchArgumentsBuilder() {
            this.page = 1;
            this.pageSize = 10;
            this.sortArgs = new SortArguments(SortArguments.SortBy.DATE, true);
        }

        public SearchArgumentsBuilder institutionId(UUID institutionId) {
            this.institutionId = institutionId;
            return this;
        }

        public SearchArgumentsBuilder careerId(UUID careerId) {
            this.careerId = careerId;
            return this;
        }

        public SearchArgumentsBuilder subjectId(UUID subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public SearchArgumentsBuilder category(String category) {
            if (category != null && !category.isEmpty())
                this.category = Category.valueOf(category.toUpperCase());
            return this;
        }

        public SearchArgumentsBuilder word(String word) {
            this.word = word;
            return this;
        }

        public SearchArgumentsBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public SearchArgumentsBuilder currentUserId(UUID currentUserId) {
            this.currentUserId = currentUserId;
            return this;
        }

        public SearchArgumentsBuilder sortBy(String sortBy) {
            if (sortBy != null && !sortBy.isEmpty())
                this.sortArgs.setSortBy(SortArguments.SortBy.valueOf(sortBy.toUpperCase()));
            return this;
        }

        public SearchArgumentsBuilder ascending(boolean ascending) {
            this.sortArgs.setAscending(ascending);
            return this;
        }

        public SearchArgumentsBuilder page(int page) {
            this.page = page;
            return this;
        }

        public SearchArgumentsBuilder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public SearchArguments build() {
            return new SearchArguments(this);
        }
    }
}
