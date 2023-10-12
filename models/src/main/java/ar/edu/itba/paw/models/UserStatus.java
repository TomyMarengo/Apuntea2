package ar.edu.itba.paw.models;

public enum UserStatus {
    ACTIVE("Active"), BANNED("Banned"), DELETED("Deleted");

    private final String status;

    UserStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


}
