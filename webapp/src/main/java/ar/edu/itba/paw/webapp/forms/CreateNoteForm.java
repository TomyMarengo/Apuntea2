package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.MaxFileSize;
import ar.edu.itba.paw.webapp.validation.ValidFileName;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.UUID;

public class CreateNoteForm {
    @NotEmpty
    @Size(min = 2, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 ]+")
    private String name;
    @ValidFileName(allowedExtensions = {".pdf"}) // TODO: Add more extensions
    @MaxFileSize(megabytes = 500)
    private MultipartFile file;
    @NotEmpty
    @Pattern(regexp = "theory|practice|exam|other")
    private String category;

    public String getName() {
        return name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public String getCategory() {
        return category;
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
}
