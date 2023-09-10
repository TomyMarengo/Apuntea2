package ar.edu.itba.apuntea.models;

import java.util.UUID;

public class User {
    private UUID userId;
    private String email;
    private String password;

    public User(final String email) {
        this.email = email;
    }

    public User(final UUID userId, final String email) {
        this.userId = userId;
        this.email = email;
    }

    public User(final UUID userId, final String email, final String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public UUID getUserId() {
        return userId;
    }
}
