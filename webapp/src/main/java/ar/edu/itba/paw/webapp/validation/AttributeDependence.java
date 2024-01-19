package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AttributeDependenceValidator.class)
@Documented
public @interface AttributeDependence {
    String message() default "{ar.edu.itba.paw.webapp.validation.AttributeDependence.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String baseField();
    String dependentField();

}