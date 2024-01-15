package ar.edu.itba.paw.webapp.forms.queries;

import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

public class DirectoryQuery extends SearchableQuery {
    @Pattern(regexp = "name|date|modified")
    @QueryParam("sortBy")
    private String sortBy = "modified";

    @QueryParam("rdir")
    private boolean rdir = false;

    public DirectoryQuery() {
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isRdir() {
        return rdir;
    }

    public void setRdir(boolean rdir) {
        this.rdir = rdir;
    }
}
