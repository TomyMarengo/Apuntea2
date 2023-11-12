package ar.edu.itba.paw.webapp.forms.search;

import ar.edu.itba.paw.webapp.validation.ValidSortParams;

import javax.validation.constraints.Pattern;

@ValidSortParams
public class SortedSearchableForm {
    private boolean ascending = false;

    @Pattern(regexp = "note|directory|theory|practice|exam|other|all")
    private String category;

    @Pattern(regexp = "score|name|date|modified")
    private String sortBy = "modified";

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getNormalizedCategory() {
        if (category.equals("all"))
            return null;
        return category;
    }

    public boolean getIsNote() {
        return !category.equals("all") && !category.equals("directory");
    }

    public boolean getAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

}
