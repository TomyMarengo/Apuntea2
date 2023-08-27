package ar.edu.itba.apuntea.models;

public class User {
    private String email;
    private String password;
    public User(final String email, final String password) {
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
