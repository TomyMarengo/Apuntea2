package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;

import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

//@EitherAttribute(fieldGroup1 = {"institutionId", "careerId", "subjectId"}, fieldGroup2 = {"rdir"})
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
