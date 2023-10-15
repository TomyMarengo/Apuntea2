package ar.edu.itba.paw.webapp.forms.user;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SearchUserForm {
    @Size(max = 30)
    @Pattern(regexp = "[a-zA-Z0-9]*") // TODO: Move regex to utils
    private String query;

    @Min(1)
    private int pageNumber = 1;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
