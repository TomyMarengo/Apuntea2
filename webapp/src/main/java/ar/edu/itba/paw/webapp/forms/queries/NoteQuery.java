package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;

import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

//@EitherAttribute(fieldGroup1 = {"institutionId", "careerId", "subjectId"}, fieldGroup2 = {"parentId", "favBy"})

public class NoteQuery extends SearchableQuery {
    @QueryParam("category")
    @Pattern(regexp = "note|directory|theory|practice|exam|other|all")
    private String category;

    @QueryParam("sortBy")
    @Pattern(regexp = "score|name|date|modified")
    private String sortBy = "modified";

    public NoteQuery() {
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
}
