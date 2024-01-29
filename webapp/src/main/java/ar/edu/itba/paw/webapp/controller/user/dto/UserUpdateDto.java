package ar.edu.itba.paw.webapp.controller.user.dto;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import ar.edu.itba.paw.webapp.validation.*;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.context.annotation.DependsOn;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@AttributeDependence(baseField = "oldPassword", dependentField = "password")
public class UserUpdateDto {
    @Pattern(regexp = RegexUtils.NAME_REGEX)
    @Size(max = 20)
    @FormDataParam("firstName")
    private String firstName;

    @Pattern(regexp = RegexUtils.NAME_REGEX)
    @Size(max = 20)
    @FormDataParam("lastName")
    private String lastName;

    @UnusedUsername
    @Size(max = 30)
    @Pattern(regexp = RegexUtils.USERNAME_REGEX)
    @FormDataParam("username")
    private String username;

    @FormDataParam("profilePicture")
    private byte[] profilePictureBytes;

    @FormDataParam("profilePicture")
    private FormDataBodyPart profilePictureDetails;

    @ValidUuid
    @FormDataParam("careerId")
    private UUID careerId;

    @Size(min = 4, max = 50)
    @Pattern(regexp = RegexUtils.PASSWORD_REGEX)
    @FormDataParam("password")
    private String password;

    @MatchesCurrentUserPassword
    private String oldPassword;

    @FormDataParam("notificationsEnabled")
    private Boolean notificationsEnabled;

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }

    public UUID getCareerId() {
        return careerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

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
}
