package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.UUID;

public class User {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Institution institution;
    private Career career;
    private Role[] roles;
    private String locale;
    private byte[] profilePicture;
    private UserStatus status;

    public User(final UUID userId, final String email) {
        this.userId = userId;
        this.email = email;
    }

    //TODO: Quitar esto si no te gusta David, es para hardcodear el listado de usuarios en el manage-users
    public User(final UUID userID, final String username, final String email, final String[] roles, final boolean status) {
        this.userId = userID;
        this.username = username;
        this.email = email;
        this.roles = Arrays.stream(roles).map(Role::getRole).toArray(Role[]::new);
        this.status = status ? UserStatus.BANNED : UserStatus.ACTIVE;
    }



    public User(final UUID userId, final String email, final String locale) {
        this.userId = userId;
        this.email = email;
        this.locale = locale;
    }


    public User(final UUID userId, final String firstName, final String lastName, final String username, final String email, final String password, final UserStatus status, final String[] roles, final String locale, final Institution institution, final Career career) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = Arrays.stream(roles).map(Role::getRole).toArray(Role[]::new);
        this.status = status;
        this.locale = locale;
        this.institution = institution;
        this.career = career;
    }

    public User (final String firstName, final String lastName, final String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public User(final UUID userId, final String email, final String locale, final String username, final String firstName, final String lastName) {
        this(userId, email, locale);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public UUID getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public Role[] getRoles() {
        return roles;
    }

    public String getLocale() {
        return locale;
    }
    public Institution getInstitution() {
        return institution;
    }

    public Career getCareer() {
        return career;
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean getIsAdmin() {
        return roles != null && Arrays.stream(roles).anyMatch(role -> role == Role.ROLE_ADMIN);
    }

    public boolean isBanned() {
        return status == UserStatus.BANNED;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getDisplayName() {
        if (username != null) {
            return username;
        } else if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return email;
    }
}
