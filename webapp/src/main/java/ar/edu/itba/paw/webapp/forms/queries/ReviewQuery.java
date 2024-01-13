package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.UUID;

// TODO: See if pagination should be inherited
public class ReviewQuery {
    @ValidUuid
    @QueryParam("doneToUser")
    private UUID doneToUser;

    @ValidUuid
    @QueryParam("noteId")
    private UUID noteId;

    @Min(1)
    @QueryParam("page")
    @DefaultValue("1")
    private int page;

    @Range(min = 4, max = 24)
    @QueryParam("pageSize")
    @DefaultValue("12")
    private int pageSize;

    public UUID getDoneToUser() {
        return doneToUser;
    }

    public void setDoneToUser(UUID doneToUser) {
        this.doneToUser = doneToUser;
    }

    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
