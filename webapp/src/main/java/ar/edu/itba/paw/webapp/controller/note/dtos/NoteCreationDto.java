package ar.edu.itba.paw.webapp.controller.note.dtos;

import ar.edu.itba.paw.webapp.forms.SearchableCreationDto;
import ar.edu.itba.paw.webapp.validation.AcceptedExtension;
import ar.edu.itba.paw.webapp.validation.AcceptedFileSize;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.hibernate.validator.constraints.NotEmpty;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.*;


public class NoteCreationDto extends SearchableCreationDto {
    @NotNull
    @FormDataParam("file")
    private byte[] file;

    //TODO Fix validators
    @NotNull
    @FormDataParam("file")
//    @AcceptedExtension(allowedExtensions = {"jpeg", "png", "jpg", "pdf", "mp3", "mp4"})
//    @AcceptedFileSize(max = 500)
    private FormDataBodyPart fileDetails;

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
        return fileDetails.getMediaType().getSubtype();
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public FormDataBodyPart getFileDetails() {
        return fileDetails;
    }

    public void setFileDetails(FormDataBodyPart fileDetails) {
        this.fileDetails = fileDetails;
    }
}
