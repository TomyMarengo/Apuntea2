package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID userId;
    private final String email;
    private String password;
    private Institution institution;
    private Career career;
    private Role[] roles;

    private String locale;

    public User(final UUID userId, final String email) {
        this.userId = userId;
        this.email = email;
    }
    public User(final UUID userId, final String email, final String locale) {
        this.userId = userId;
        this.email = email;
        this.locale = locale;
    }

    public User(final UUID userId, final String email, final String password, final String[] roles, final String locale, final Institution institution, final Career career) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.roles = Arrays.stream(roles).map(Role::getRole).toArray(Role[]::new);
        this.locale = locale;
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
}
