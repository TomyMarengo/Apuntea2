package ar.edu.itba.paw.webapp.dto;

public class ApiErrorDto {
    private String message;

    public ApiErrorDto() {
    }

    public ApiErrorDto(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
