package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID userId;
    private String email;
    private String password;

    private Role[] roles;

    public User(final UUID userId, final String email) {
        this.userId = userId;
        this.email = email;
    }

    public User(final UUID userId, final String email, final String password, final String[] roles) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.roles = Arrays.stream(roles).map(Role::getRole).toArray(Role[]::new);
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
}
