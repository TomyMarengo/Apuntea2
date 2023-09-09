package ar.edu.itba.apuntea.webapp.forms;

import ar.edu.itba.apuntea.webapp.validation.ValidFileName;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class CreateNoteForm {

    @NotEmpty
    @Size(min = 5, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 ]+")
    private String name;
    @ValidFileName(allowedExtensions = {".pdf"}) // TODO: Add more extensions
    private MultipartFile file;
    @NotEmpty
    private String institution;
    @NotEmpty
    private String career;
    @NotEmpty
    private String subject;
    @NotEmpty
    @Pattern(regexp = "theory|practice|exam")
    private String category;
    @NotEmpty
    @Email
    private String email;

    // All getters
    public String getName() {
        return name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public String getInstitution() {
        return institution;
    }

    public String getCareer() {
        return career;
    }

    public String getSubject() {
        return subject;
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

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
