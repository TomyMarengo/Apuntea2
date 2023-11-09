package ar.edu.itba.paw.models.search;

public class SortArguments {
    private boolean ascending;

    private SortBy sortBy;

    public SortArguments(SortBy sortBy, boolean ascending) {
        this.sortBy = sortBy;
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public enum SortBy {
        NAME, SCORE, DATE, MODIFIED
    }
}
