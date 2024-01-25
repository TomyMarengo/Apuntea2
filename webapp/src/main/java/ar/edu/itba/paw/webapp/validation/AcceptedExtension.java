package ar.edu.itba.paw.webapp.validation;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcceptedExtension.AcceptedExtensionValidator.class)
@Documented
public @interface AcceptedExtension {
    String message() default "{ar.edu.itba.paw.webapp.validation.AcceptedExtension.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedExtensions() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        AcceptedExtension[] acceptedExtension();
    }

    class AcceptedExtensionValidator implements ConstraintValidator<AcceptedExtension, FormDataBodyPart> {

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
}
