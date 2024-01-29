package ar.edu.itba.paw.models.exceptions;

public class FileSizeException extends RuntimeException{
    private int maxSize;

    public FileSizeException(final int maxSize) {
        super();
        this.maxSize = maxSize;
    }
    public FileSizeException(Throwable var1) {
        super(var1);
    }

    public int getMaxSize() {
        return maxSize;
    }
}
