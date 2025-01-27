package ar.edu.itba.paw.webapp.controller.note.dtos;

import ar.edu.itba.paw.webapp.forms.SearchableCreationForm;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.hibernate.validator.constraints.NotEmpty;
import ar.edu.itba.paw.webapp.forms.RegexUtils;

import javax.validation.constraints.*;


public class NoteCreationForm extends SearchableCreationForm {
    @NotNull(message = "{error.param.empty}")
    @FormDataParam("file")
    private byte[] file;

    @NotNull(message = "{error.param.empty}")
    @FormDataParam("file")
    private FormDataBodyPart fileDetails;

    @NotEmpty(message = "{error.param.empty}")
    @Pattern(regexp = RegexUtils.CATEGORY_REGEX, message = "{validation.category}")
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
