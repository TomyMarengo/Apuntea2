package ar.edu.itba.paw.webapp.validation;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class AcceptedFileSizeValidator implements ConstraintValidator<AcceptedFileSize, FormDataBodyPart> {
    private int max;
    @Override
    public void initialize(AcceptedFileSize constraintAnnotation) {
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(FormDataBodyPart value, ConstraintValidatorContext context) {
        return value.getContentDisposition().getSize() / 1024 / 1024 <= max;
    }
}