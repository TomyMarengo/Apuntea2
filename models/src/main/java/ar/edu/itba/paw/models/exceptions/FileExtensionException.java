package ar.edu.itba.paw.models.exceptions;

public class FileExtensionException extends RuntimeException{
    private String[] allowedExtensions;
    public FileExtensionException(String[] allowedExtensions) {
        super();
        this.allowedExtensions = allowedExtensions;
    }
    public FileExtensionException() {
        super();
    }
    public FileExtensionException(Throwable var1) {
        super(var1);
    }

    public String[] getAllowedExtensions(){
        return allowedExtensions;
    }

}
