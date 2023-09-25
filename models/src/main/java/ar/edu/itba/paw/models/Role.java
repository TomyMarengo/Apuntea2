package ar.edu.itba.paw.models;

public enum Role {
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Role getRole(String s) {
        for (Role role : values()) {
            if (role.getRole().equals(s))
                return role;
        }
        throw new IllegalArgumentException();
    }
}
