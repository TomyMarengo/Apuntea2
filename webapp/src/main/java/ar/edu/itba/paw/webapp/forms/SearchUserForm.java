package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SearchUserForm {

    @Size(max = 30)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    //TODO add more restrictions
    private String username;

    @NotNull
    @Min(1)
    private Integer pageNumber = 1;

    @NotNull
    @Range(min = 12, max = 36)
    private Integer pageSize = 12;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
