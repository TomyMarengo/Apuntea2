package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import javax.ws.rs.QueryParam;
import java.util.UUID;

// TODO: See if pagination should be inherited
public class ReviewQuery extends PageableQuery{
    @ValidUuid
    @QueryParam("doneToUser")
    private UUID doneToUser;

    @ValidUuid
    @QueryParam("noteId")
    private UUID noteId;


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

}
