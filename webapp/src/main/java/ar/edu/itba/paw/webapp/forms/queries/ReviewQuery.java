package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import javax.ws.rs.QueryParam;
import java.util.UUID;

public class ReviewQuery extends PageableQuery{
    @ValidUuid
    @QueryParam("doneToUser")
    private UUID targetUser;

    @ValidUuid
    @QueryParam("noteId")
    private UUID noteId;

    @ValidUuid
    @QueryParam("userId")
    private UUID userId;


    public UUID getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(UUID targetUser) {
        this.targetUser = targetUser;
    }

    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
