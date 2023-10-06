package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.List;
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

    public User(final UUID userId, final String email) {
        this.userId = userId;
        this.email = email;
    }
    public User(final UUID userId, final String email, final String locale) {
        this.userId = userId;
        this.email = email;
        this.locale = locale;
    }

    public User(final UUID userId, final String firstName, final String lastName, final String username, final String email, final String password, final String[] roles, final String locale, final Institution institution, final Career career) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = Arrays.stream(roles).map(Role::getRole).toArray(Role[]::new);
        this.locale = locale;
        this.institution = institution;
        this.career = career;
    }

    public User (final String firstName, final String lastName, final String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
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
}
