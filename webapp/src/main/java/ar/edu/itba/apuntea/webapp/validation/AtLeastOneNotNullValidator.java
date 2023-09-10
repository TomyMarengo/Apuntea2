package ar.edu.itba.apuntea.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {

    private String[] fieldNames;

    @Override
    public void initialize(AtLeastOneNotNull constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fieldNames();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Si el objeto completo es null, no se puede realizar la validación
        }

        for (String fieldName : fieldNames) {
            Object fieldValue = FieldValueUtil.getFieldValue(value, fieldName);
            if (fieldValue != null) {
                return true; // Al menos un campo no es null, la validación es exitosa
            }
        }

        return false; // Ningún campo es diferente de null, la validación falla
    }
}