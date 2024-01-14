package ar.edu.itba.paw.webapp.validation;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class AcceptedExtensionValidator implements ConstraintValidator<AcceptedExtension, FormDataBodyPart> {

    private String[] extensions;
    @Override
    public void initialize(AcceptedExtension constraintAnnotation) {
        extensions = constraintAnnotation.allowedExtensions();
    }

    @Override
    public boolean isValid(FormDataBodyPart value, ConstraintValidatorContext context) {
        return Arrays.asList(extensions).contains(value.getMediaType().getSubtype());
    }
}
