package ar.edu.itba.apuntea.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidFileNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileName {
    String message() default
            "{ar.edu.itba.apuntea.webapp.validation.ValidFileName.message}";
    String[] allowedExtensions() default {};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}