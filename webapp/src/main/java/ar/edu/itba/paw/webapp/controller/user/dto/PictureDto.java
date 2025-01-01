package ar.edu.itba.paw.webapp.controller.user.dto;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.NotNull;

public class PictureDto {
    @NotNull(message = "{error.param.empty}")
    @FormDataParam("profilePicture")
    private byte[] profilePictureBytes;

    @NotNull(message = "{error.param.empty}")
    @FormDataParam("profilePicture")
    private FormDataBodyPart profilePictureDetails;


    public byte[] getProfilePictureBytes() {
        return profilePictureBytes;
    }

    public void setProfilePictureBytes(byte[] profilePictureBytes) {
        this.profilePictureBytes = profilePictureBytes;
    }

    public FormDataBodyPart getProfilePictureDetails() {
        return profilePictureDetails;
    }

    public void setProfilePictureDetails(FormDataBodyPart profilePictureDetails) {
        this.profilePictureDetails = profilePictureDetails;
    }

    public String getProfilePictureExtension() {
        if (profilePictureDetails == null)
            return null;
        return profilePictureDetails.getMediaType().getSubtype();
    }
}
