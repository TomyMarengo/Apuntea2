package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;
import javax.ws.rs.QueryParam;
import java.util.UUID;

@EitherAttribute(fieldGroup1 = {"targetUser"}, fieldGroup2 = {"userId", "noteId"})
public class ReviewQuery extends PageableQuery{
    @QueryParam("targetUser")
    private UUID targetUser;

    @QueryParam("noteId")
    private UUID noteId;

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
