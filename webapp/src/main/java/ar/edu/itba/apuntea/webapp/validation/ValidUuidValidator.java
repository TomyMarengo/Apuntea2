package ar.edu.itba.apuntea.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;
import java.util.regex.Pattern;

public class ValidUuidValidator implements ConstraintValidator<ValidUuid, UUID> {

    private Pattern pattern;

    @Override
    public void initialize(ValidUuid constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.regex());
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.toString().isEmpty()) {
            return false; // Reject empty values
        }

        return pattern.matcher(value.toString()).matches();
    }
}