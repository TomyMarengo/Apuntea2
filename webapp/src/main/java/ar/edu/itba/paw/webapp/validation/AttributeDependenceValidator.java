package ar.edu.itba.paw.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AttributeDependenceValidator implements ConstraintValidator<AttributeDependence, Object> {
    private String baseField;
    private String dependentField;

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
        Object dependentFieldValue = FieldValueUtil.getFieldValue(value, dependentField);
        return baseFieldValue == null || dependentFieldValue != null;
    }
}