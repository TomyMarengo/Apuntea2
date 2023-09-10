package ar.edu.itba.apuntea.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidUuidValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUuid {
    String message() default "{ar.edu.itba.apuntea.webapp.validation.ValidUuid.message}";
    String regex() default "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}