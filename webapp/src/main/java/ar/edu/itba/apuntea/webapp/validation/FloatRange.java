package ar.edu.itba.apuntea.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FloatRangeValidator.class)
@Documented
public @interface FloatRange {
    String message() default "{ar.edu.itba.apuntea.webapp.validation.FloatRange.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    float min() default 0;
    float max() default 5;
}