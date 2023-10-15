package ar.edu.itba.paw.models.exceptions.user;

public class RequiredAdminException extends RuntimeException {
    public RequiredAdminException() {
    }
    public RequiredAdminException(Throwable var1) {
        super(var1);
    }
}
