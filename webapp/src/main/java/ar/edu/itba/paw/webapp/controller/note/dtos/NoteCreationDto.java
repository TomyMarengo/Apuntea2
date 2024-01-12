package ar.edu.itba.paw.webapp.controller.note.dtos;

import ar.edu.itba.paw.webapp.forms.SearchableCreationDto;
import ar.edu.itba.paw.webapp.validation.MaxFileSize;
import ar.edu.itba.paw.webapp.validation.ValidFileName;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.*;
import java.io.InputStream;


public class NoteCreationDto extends SearchableCreationDto {
    @NotNull
    @FormDataParam("file")
    private byte[] file;

    @NotNull
    @FormDataParam("file")
    private FormDataBodyPart imageDetails;

    @NotEmpty
    @Pattern(regexp = RegexUtils.CATEGORY_REGEX)
    @FormDataParam("category")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMimeType() {
        return imageDetails.getMediaType().getSubtype();
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public FormDataBodyPart getImageDetails() {
        return imageDetails;
    }

    public void setImageDetails(FormDataBodyPart imageDetails) {
        this.imageDetails = imageDetails;
    }
}
