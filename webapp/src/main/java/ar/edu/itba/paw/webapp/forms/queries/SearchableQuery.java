package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.UUID;

@EitherAttribute(fieldGroup1 = {"institutionId", "careerId", "subjectId"}, fieldGroup2 = {"parentId", "favBy"}, message = "invalidAttributeCombination")
public class SearchableQuery extends PageableQuery { //TODO: Add error messages
    @DefaultValue("true")
    @QueryParam("asc")
    private boolean ascending;

    @ValidUuid
    @QueryParam("userId")
    private UUID userId;

    @Size(max = 50)
    @QueryParam("word")
    private String word;

    @ValidUuid
    @QueryParam("institutionId")
    private UUID institutionId;

    @ValidUuid
    @QueryParam("careerId")
    private UUID careerId;

    @ValidUuid
    @QueryParam("subjectId")
    private UUID subjectId;

    @ValidUuid
    @QueryParam("parentId")
    private UUID parentId;

    @ValidUuid
    @QueryParam("favBy")
    private UUID favBy;


    public boolean getAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public UUID getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(UUID institutionId) {
        this.institutionId = institutionId;
    }

    public UUID getCareerId() {
        return careerId;
    }

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }


    public UUID getFavBy() {
        return favBy;
    }

    public void setFavBy(UUID favBy) {
        this.favBy = favBy;
    }


}
