package ar.edu.itba.paw.models.user;

public enum UserStatus {
    ACTIVE("active"), BANNED("banned"), DELETED("deleted");

    private final String status;

    UserStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


}
