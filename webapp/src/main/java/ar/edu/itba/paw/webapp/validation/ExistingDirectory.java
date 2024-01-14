package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingDirectoryValidator.class})
public @interface ExistingDirectory {
    String message() default "{ar.edu.itba.paw.webapp.validation.ExistingDirectory.message}";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ExistingDirectory[] existingDirectory();
    }
}
