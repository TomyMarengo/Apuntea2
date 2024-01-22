package ar.edu.itba.paw.webapp.controller.user.dto;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

public class UserResponseDto {
    private UUID id;
//    private String email;
    private String firstName;
    private String lastName;
    private String locale;
    private String username;
    private String status;
    private Boolean notificationsEnabled;


    // TODO: Add roles?
    private URI self;
    private URI career;
    private URI institution;
    private URI profilePicture;
    private URI noteFavorites;
    private URI directoryFavorites;
    private URI subjectFavorites;
    private URI following;
    private URI notes;
    private URI noteGroups;
    private URI reviewsReceived;

    public static UserResponseDto fromUser(final User user, final UriInfo uriInfo) {
        final UserResponseDto userDto = new UserResponseDto();
        userDto.id = user.getUserId();
//        userDto.email = user.getEmail();
        userDto.firstName = user.getFirstName();
        userDto.lastName = user.getLastName();
        userDto.locale = user.getLocale().toString();
        userDto.username = user.getDisplayName();
        userDto.status = user.getStatus().toString();
        userDto.notificationsEnabled = user.getNotificationsEnabled();
        userDto.self = uriInfo.getBaseUriBuilder().path("users").path(user.getUserId().toString()).build();

        UriBuilder builder = uriInfo.getBaseUriBuilder();

        final Career career = user.getCareer();
        if (career != null) {
            userDto.institution = builder.path("institutions").path(career.getInstitutionId().toString()).build();
            userDto.career = builder.path("careers").path(career.getCareerId().toString()).build();
        }
        // TODO: Check if this is efficient
        if (user.getProfilePicture() != null)
            userDto.profilePicture = uriInfo.getBaseUriBuilder().path("pictures").path(user.getProfilePicture().getImageId().toString()).build();
        userDto.noteFavorites = uriInfo.getBaseUriBuilder().path("notes").queryParam("favBy", user.getUserId()).build();
        userDto.directoryFavorites = uriInfo.getBaseUriBuilder().path("directories").queryParam("favBy", user.getUserId()).build();
        userDto.subjectFavorites = uriInfo.getBaseUriBuilder().path("subjects").queryParam("favBy", user.getUserId()).build();
        userDto.following = uriInfo.getBaseUriBuilder().path("users").path(user.getUserId().toString()).path("following").build();
        userDto.notes = uriInfo.getBaseUriBuilder().path("notes").queryParam("userId", user.getUserId().toString()).build();
        userDto.noteGroups = uriInfo.getBaseUriBuilder().path("notes").queryParam("owner", user.getUserId().toString()).queryParam("grouped").build();
        // TODO: Change
        userDto.reviewsReceived = uriInfo.getBaseUriBuilder().path("users").path(user.getUserId().toString()).path("reviews").build();
        return userDto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public void setCareer(URI career) {
        this.career = career;
    }

    public void setInstitution(URI institution) {
        this.institution = institution;
    }

    public void setProfilePicture(URI profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setNoteFavorites(URI noteFavorites) {
        this.noteFavorites = noteFavorites;
    }

    public void setDirectoryFavorites(URI directoryFavorites) {
        this.directoryFavorites = directoryFavorites;
    }

    public void setSubjectFavorites(URI subjectFavorites) {
        this.subjectFavorites = subjectFavorites;
    }

    public void setFollowing(URI following) {
        this.following = following;
    }

    public void setNotes(URI notes) {
        this.notes = notes;
    }

    public void setNoteGroups(URI noteGroups) {
        this.noteGroups = noteGroups;
    }

    public void setReviewsReceived(URI reviewsReceived) {
        this.reviewsReceived = reviewsReceived;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocale() {
        return locale;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public URI getSelf() {
        return self;
    }

    public URI getCareer() {
        return career;
    }

    public URI getInstitution() {
        return institution;
    }

    public URI getProfilePicture() {
        return profilePicture;
    }

    public URI getNoteFavorites() {
        return noteFavorites;
    }

    public URI getDirectoryFavorites() {
        return directoryFavorites;
    }

    public URI getSubjectFavorites() {
        return subjectFavorites;
    }

    public URI getFollowing() {
        return following;
    }

    public URI getNotes() {
        return notes;
    }

    public URI getNoteGroups() {
        return noteGroups;
    }

    public URI getReviewsReceived() {
        return reviewsReceived;
    }

}

