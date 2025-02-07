package ar.edu.itba.paw.webapp.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Locale;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AttributeDependence.AttributeDependenceValidator.class)
@Documented
public @interface AttributeDependence {
    String message() default "validation.attributeDependence";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String baseField();
    String[] dependentField();

    class AttributeDependenceValidator implements ConstraintValidator<AttributeDependence, Object> {
        @Autowired
        private MessageSource messageSource;

        private String baseField;
        private String[] dependentField; /* by default OR */

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
            if (baseFieldValue != null && !dependentFieldPresent) {
               String dependentFieldString = String.join(", ", dependentField);
               Locale locale = LocaleContextHolder.getLocale();
               String errorMessage = messageSource.getMessage("validation.attributeDependence", new Object[]{baseField, dependentFieldString}, locale);
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errorMessage)
                        .addPropertyNode(dependentField[0])
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }

}