package ar.edu.itba.paw.webapp.validation;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AcceptedFileSize.AcceptedFileSizeValidator.class)
@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AcceptedFileSize {
    String message() default "{ar.edu.itba.paw.webapp.validation.AcceptedFileSize.message}";
    int max() default Integer.MAX_VALUE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        AcceptedFileSize[] acceptedFileSize();
    }

    class AcceptedFileSizeValidator implements ConstraintValidator<AcceptedFileSize, FormDataBodyPart> {
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
}
