package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EitherAttributeValidator.class)
@Documented
public @interface EitherAttribute {
    String message() default "{ar.edu.itba.paw.webapp.validation.EitherAttribute.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fieldGroup1();
    String[] fieldGroup2();
}
