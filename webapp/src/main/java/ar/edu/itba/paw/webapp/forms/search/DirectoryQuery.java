package ar.edu.itba.paw.webapp.forms.search;

import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

public class DirectoryQuery extends SearchableQuery {
    @Pattern(regexp = "name|date|modified")
    @QueryParam("sortBy")
    private String sortBy = "modified";

    public DirectoryQuery() {
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
