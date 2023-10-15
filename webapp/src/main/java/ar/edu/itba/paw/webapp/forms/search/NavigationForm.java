package ar.edu.itba.paw.webapp.forms.search;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class NavigationForm {
    @Pattern(regexp = "directory|theory|practice|exam|other|")
    private String category = "";

    @Pattern(regexp = "score|name|date")
    private String sortBy = "score";

    @NotNull
    private Boolean ascending = false;

    @NotNull
    @Min(1)
    private Integer pageNumber = 1;

    @NotNull
    @Range(min = 4, max = 24)
    private Integer pageSize = 12;

//    @Pattern(regexp = "[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑüÜ]*")
    @Size(max = 50)
    private String word;

    public Boolean getAscending() {
        return ascending;
    }

    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
