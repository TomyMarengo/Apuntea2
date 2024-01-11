package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;

public class ApiErrorDto {
    private String message;


    public ApiErrorDto()  {
    }

    public ApiErrorDto( String message) {
        this.message = message;
    }

    public static ApiErrorDto fromValidationException(final ConstraintViolation<?> constraintViolation) {
        final ApiErrorDto apiErrorDto = new ApiErrorDto();
        apiErrorDto.message = constraintViolation.getMessage();
        return apiErrorDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
