package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.UUID;
import java.util.regex.Pattern;

@Documented
@Constraint(validatedBy = ValidUuid.ValidUuidValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUuid {
    String message() default "validation.uuid";
    String regex() default "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class ValidUuidValidator implements ConstraintValidator<ValidUuid, UUID> {

        private Pattern pattern;

        @Override
        public void initialize(ValidUuid constraintAnnotation) {
            this.pattern = Pattern.compile(constraintAnnotation.regex());
        }

        @Override
        public boolean isValid(UUID value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null || value.toString().isEmpty()) {
                return true;
            }

            return pattern.matcher(value.toString()).matches();
        }
    }
}