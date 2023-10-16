package ar.edu.itba.paw.webapp.forms.note;

import ar.edu.itba.paw.webapp.forms.CreateSearchableForm;
import ar.edu.itba.paw.webapp.validation.MaxFileSize;
import ar.edu.itba.paw.webapp.validation.ValidFileName;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.*;


public class CreateNoteForm extends CreateSearchableForm {
    @ValidFileName(allowedExtensions = {".pdf", ".png", ".jpg", ".jpeg", ".mp3", ".mp4"})
    @MaxFileSize(megabytes = 500, allowEmptyFiles = false)
    private MultipartFile file;

    @NotEmpty
    @Pattern(regexp = RegexUtils.CATEGORY_REGEX)
    private String category;
    public MultipartFile getFile() {
        return file;
    }

    public String getCategory() {
        return category;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
