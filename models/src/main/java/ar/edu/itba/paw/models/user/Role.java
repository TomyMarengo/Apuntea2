package ar.edu.itba.paw.models.user;

public enum Role {
    ROLE_ANONYMOUS("ROLE_ANONYMOUS", "ANONYMOUS"),
    ROLE_STUDENT("ROLE_STUDENT", "STUDENT"),
    ROLE_MODERATOR("ROLE_MODERATOR", "MODERATOR"),
    ROLE_VERIFY("ROLE_VERIFY", "VERIFY"),
    ROLE_ADMIN("ROLE_ADMIN", "ADMIN");

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
