package ar.edu.itba.paw.webapp.forms.institutional;

import ar.edu.itba.paw.webapp.validation.DetachableSubject;
import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@DetachableSubject
public class UnlinkSubjectForm {

    @ValidUuid
    @NotNull
    private UUID subjectId;

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }
}
