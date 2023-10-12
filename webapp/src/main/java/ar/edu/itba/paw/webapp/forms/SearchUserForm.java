package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SearchUserForm {

    @Size(max = 30)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String query;

    @NotNull
    @Min(1)
    private Integer pageNumber = 1;

    @NotNull
    @Range(min = 12, max = 36)
    private Integer pageSize = 12;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

}
