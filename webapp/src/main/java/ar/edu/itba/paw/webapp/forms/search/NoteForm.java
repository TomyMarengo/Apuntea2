package ar.edu.itba.paw.webapp.forms.search;

import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

public class NoteForm extends SortedSearchableForm {
    @QueryParam("category")
    @Pattern(regexp = "note|directory|theory|practice|exam|other|all")
    private String category;

    @QueryParam("sortBy")
    @Pattern(regexp = "score|name|date|modified")
    private String sortBy = "modified";

    public NoteForm() {
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
