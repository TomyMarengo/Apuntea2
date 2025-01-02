package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;

import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

//@EitherAttribute(fieldGroup1 = {"institutionId", "careerId", "subjectId"}, fieldGroup2 = {"rdir"})
public class DirectoryQuery extends SearchableQuery {
    @Pattern(regexp = "name|date|modified", message = "{error.searchable.invalidSort}")
    @QueryParam("sortBy")
    private String sortBy = "modified";

    @QueryParam("rdir")
    private Boolean rdir = null;

    public DirectoryQuery() {
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean isRdir() {
        return rdir;
    }

    public void setRdir(Boolean rdir) {
        this.rdir = rdir;
    }
}
