package ar.edu.itba.apuntea.models;

import java.util.UUID;

public class User {
    private long userId;
    private String email;
    private String password;

    public User(final long userId, final String email, final String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

}
