package ar.edu.itba.paw.webapp.forms.search;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public class SearchForm {
    @ValidUuid
    private UUID institutionId;

    @ValidUuid
    private UUID careerId;

    @ValidUuid
    private UUID subjectId;

    @ValidUuid
    private UUID userId;

    @Pattern(regexp = "note|directory|theory|practice|exam|other|all")
    private String category = "note";

    @Pattern(regexp = "score|name|date|modified")
    private String sortBy = "modified";

    private boolean ascending = false;

    @Min(1)
    private int pageNumber = 1;

    @Range(min = 4, max = 24)
    private int pageSize = 12;

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

    public boolean getAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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

    public String getNormalizedCategory() {
        if (category.equals("all"))
            return null;
        return category;
    }

    public boolean getIsNote() {
        return !category.equals("all") && !category.equals("directory");
    }
}
