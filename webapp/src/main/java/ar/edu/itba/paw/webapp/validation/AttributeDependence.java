package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AttributeDependence.AttributeDependenceValidator.class)
@Documented
public @interface AttributeDependence {
    String message() default "{ar.edu.itba.paw.webapp.validation.AttributeDependence.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String baseField();
    String[] dependentField();

    class AttributeDependenceValidator implements ConstraintValidator<AttributeDependence, Object> {
        private String baseField;
        private String[] dependentField; //by default OR

        @Override
        public void initialize(AttributeDependence constraintAnnotation) {
            this.baseField = constraintAnnotation.baseField();
            this.dependentField = constraintAnnotation.dependentField();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            Object baseFieldValue = FieldValueUtil.getFieldValue(value, baseField);
            boolean dependentFieldPresent = false;
            for (String dependentField : dependentField) {
                Object dependentFieldValue = FieldValueUtil.getFieldValue(value, dependentField);
                if (dependentFieldValue != null) {
                    dependentFieldPresent = true;
                    break;
                }
            }
            return baseFieldValue == null || dependentFieldPresent;
        }
    }

}