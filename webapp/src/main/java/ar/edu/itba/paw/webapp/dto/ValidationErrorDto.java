package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;

public class ValidationErrorDto {

    private String message;
    private String path;

    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> constraintViolation) {
        final ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        validationErrorDto.message = constraintViolation.getMessage();
        validationErrorDto.path = constraintViolation.getPropertyPath().toString();
        return validationErrorDto;
    }
}
