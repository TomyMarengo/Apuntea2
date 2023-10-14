package ar.edu.itba.paw.models.user;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;

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
    private UserStatus status;

    public User(final UUID userId, final String email) {
        this.userId = userId;
        this.email = email;
    }

    public User(final UUID userId, final String email, final String locale) {
        this(userId, email);
        this.locale = locale;
    }

    public User(final UUID userId, final String firstName, final String lastName, final String username, final String email, final String password, final UserStatus status, final String[] roles, final String locale, final Institution institution, final Career career) {
        this(userId, email, locale);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = Arrays.stream(roles).map(Role::getRole).toArray(Role[]::new);
        this.status = status;
        this.institution = institution;
        this.career = career;
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

    public void setProfileData(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }
}
