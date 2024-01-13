package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.UUID;

public class SearchableQuery { //TODO: Add error messages
    @DefaultValue("true")
    @QueryParam("asc")
    private boolean ascending;

    @ValidUuid
    @QueryParam("userId")
    private UUID userId;

    @Min(1)
    @QueryParam("page")
    @DefaultValue("1")
    private int page;

    @Range(min = 4, max = 24)
    @QueryParam("pageSize")
    @DefaultValue("12")
    private int pageSize;

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

    public int getPageNumber() {
        return page;
    }

    public void setPageNumber(int pageNumber) {
        this.page = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public SearchableQuery setParentId(UUID parentId) {
        this.parentId = parentId;
        return this;
    }


    public UUID getFavBy() {
        return favBy;
    }

    public SearchableQuery setFavBy(UUID favBy) {
        this.favBy = favBy;
        return this;
    }


}
