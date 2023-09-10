package ar.edu.itba.apuntea.webapp.forms;

import ar.edu.itba.apuntea.webapp.validation.MaxFileSize;
import ar.edu.itba.apuntea.webapp.validation.ValidFileName;
import ar.edu.itba.apuntea.webapp.validation.ValidUuid;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.UUID;

public class CreateNoteForm {

    @NotEmpty
    @Size(min = 5, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 ]+")
    private String name;
    @ValidFileName(allowedExtensions = {".pdf"}) // TODO: Add more extensions
    @MaxFileSize(megabytes = 10) // 10 MB
    private MultipartFile file;
    //TODO: Add validation for institution, career and subject
    @ValidUuid
    private UUID institutionId;
    @ValidUuid
    private UUID careerId;
    @ValidUuid
    private UUID subjectId;
    @NotEmpty
    @Pattern(regexp = "theory|practice|exam|other")
    private String category;
    @NotEmpty
    @Email
    private String email;

    // All getters

    public UUID getSubjectId() {
        return subjectId;
    }
    public String getName() {
        return name;
    }

    public MultipartFile getFile() {
        return file;
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

    public String getCategory() {
        return category;
    }

    public String getEmail() {
        return email;
    }

    // All setters

    public void setName(String name) {
        this.name = name;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }
}
