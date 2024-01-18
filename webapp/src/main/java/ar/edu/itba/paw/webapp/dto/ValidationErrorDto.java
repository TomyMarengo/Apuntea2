package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

public class ValidationErrorDto {
    private String message;
    private Object field;

    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> constraintViolation) {
        final ValidationErrorDto validationErrorDto = new ValidationErrorDto();
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

//    public Object getValue() {
//        return value;
//    }
//
//    public void setValue(Object value) {
//        this.value = value;
//    }
}
