package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcceptedExtensionValidator.class)
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
}
