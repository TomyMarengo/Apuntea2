package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.FloatRange;
import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public class SearchNotesForm {
    @ValidUuid
    private UUID institutionId;
    @ValidUuid
    private UUID careerId;
    @ValidUuid
    private UUID subjectId;
    @Pattern(regexp = "theory|practice|exam|other")
    private String category;
    @Pattern(regexp = "score|name|date")
    private String sortBy;
    @NotNull
    private Boolean ascending = true;
    @NotNull
    private Integer page = 1;
    @NotNull
    private Integer pageSize = 10;
    @Pattern(regexp = "[a-zA-Z0-9 ]*")
    @Size(max = 50)
    private String word;

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

    public Boolean getAscending() {
        return ascending;
    }

    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
