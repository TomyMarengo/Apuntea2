package ar.edu.itba.paw.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FloatRangeValidator implements ConstraintValidator<FloatRange, Float> {
    private float min;
    private float max;

    @Override
    public void initialize(FloatRange constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        return value == null || (value >= min && value <= max);
    }
}