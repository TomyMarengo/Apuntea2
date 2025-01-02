package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.UUID;

@EitherAttribute(fieldGroup1 = {"institutionId", "careerId", "subjectId"}, fieldGroup2 = {"parentId", "favBy"})
public class SearchableQuery extends PageableQuery {
    @DefaultValue("true")
    @QueryParam("asc")
    private boolean ascending;

    @QueryParam("userId")
    private UUID userId;

    @Size(max = 50, message = "{error.param.size}")
    @QueryParam("word")
    private String word;

    @QueryParam("institutionId")
    private UUID institutionId;

    @QueryParam("careerId")
    private UUID careerId;

    @QueryParam("subjectId")
    private UUID subjectId;

    @QueryParam("parentId")
    private UUID parentId;

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
