package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.InstitutionCareerRelation;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

@InstitutionCareerRelation
public class InstitutionCareerPathParams {
    @PathParam("institutionId")
    private UUID institutionId;

    @PathParam("careerId")
    private UUID careerId;

    public UUID getInstitutionId() {
        return institutionId;
    }

    public UUID getCareerId() {
        return careerId;
    }

    public void setInstitutionId(UUID institutionId) {
        this.institutionId = institutionId;
    }

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }
}
