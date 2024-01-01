package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxFileSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSize {
    String message() default "{ar.edu.itba.paw.webapp.validation.MaxFileSize.message}";
    long megabytes(); // Maximum value in megabytes
    boolean allowEmptyFiles(); // If true, empty files can be uploaded
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}