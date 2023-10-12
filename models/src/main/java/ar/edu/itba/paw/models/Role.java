package ar.edu.itba.paw.models;

public enum Role {
    ROLE_STUDENT("ROLE_STUDENT", "Student"),
    ROLE_MODERATOR("ROLE_MODERATOR", "Moderator"),
    ROLE_ADMIN("ROLE_ADMIN", "Admin");

    private final String role;
    private final String shortName;

    Role(String role, String shortName){
        this.role = role;
        this.shortName = shortName;
    }

    public String getRole() {
        return role;
    }
    public String getShortName() {
        return shortName;
    }
    public static Role getRole(String s) {
        for (Role role : values()) {
            if (role.getRole().equals(s))
                return role;
        }
        throw new IllegalArgumentException();
    }
}
