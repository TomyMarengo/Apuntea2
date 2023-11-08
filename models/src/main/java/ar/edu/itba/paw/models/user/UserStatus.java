package ar.edu.itba.paw.models.user;

public enum UserStatus {
    ACTIVE("active"), BANNED("banned"), DELETED("deleted");

    private final String status;

    UserStatus(String status){
        this.status = status;
    }

    public static UserStatus fromString(String status) {
        if (status != null) {
            for (UserStatus userStatus : UserStatus.values()) {
                if (status.equalsIgnoreCase(userStatus.status)) {
                    return userStatus;
                }
            }
        }
        return null;
    }

    public String getStatus() {
        return status;
    }


}
