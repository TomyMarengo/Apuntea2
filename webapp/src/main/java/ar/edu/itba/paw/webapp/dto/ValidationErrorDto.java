package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.validation.InstitutionCareerRelation;

import javax.validation.ConstraintViolation;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

public class ValidationErrorDto {
    private String message;
    private Object field;
    private boolean notFound = false;

    private static final Class<?>[] notFoundValidators = {InstitutionCareerRelation.class}; // Validators that when they fail, the error is "not found"

    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> constraintViolation) {
        final ValidationErrorDto validationErrorDto = new ValidationErrorDto();

        Class<?> c = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType();
        for (Class<?> validator : notFoundValidators) {
            if (validator.isAssignableFrom(c)) {
                validationErrorDto.notFound = true;
                break;
            }
        }

        validationErrorDto.message = constraintViolation.getMessage();
        validationErrorDto.field = StreamSupport.stream(() -> constraintViolation.getPropertyPath().spliterator(), Spliterator.ORDERED, false)
                .reduce((first, second) -> second).orElseThrow(RuntimeException::new).getName();
//        apiErrorDto.value = constraintViolation.getInvalidValue();
        return validationErrorDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public boolean isNotFound() {
        return notFound;
    }

//    public Object getValue() {
//        return value;
//    }
//
//    public void setValue(Object value) {
//        this.value = value;
//    }
}
