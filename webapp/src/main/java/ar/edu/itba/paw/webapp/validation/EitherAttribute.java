package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Inherited
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EitherAttribute.EitherAttributeValidator.class)
@Documented
public @interface EitherAttribute {
    String message() default "{ar.edu.itba.paw.webapp.validation.EitherAttribute.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fieldGroup1();
    String[] fieldGroup2();

    boolean allowNeither() default true;

    class EitherAttributeValidator implements ConstraintValidator<EitherAttribute, Object> {
        private String[] fieldGroup1;
        private String[] fieldGroup2;

        private boolean allowNeither;

        @Override
        public void initialize(EitherAttribute constraintAnnotation) {
            this.fieldGroup1 = constraintAnnotation.fieldGroup1();
            this.fieldGroup2 = constraintAnnotation.fieldGroup2();
            this.allowNeither = constraintAnnotation.allowNeither();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            int count1 = 0;
            for (String fieldName : fieldGroup1) {
                Object fieldValue = FieldValueUtil.getFieldValue(value, fieldName);
                if (fieldValue != null) {
                    count1++;
                }
            }

            int count2 = 0;
            for (String fieldName : fieldGroup2) {
                Object fieldValue = FieldValueUtil.getFieldValue(value, fieldName);
                if (fieldValue != null) {
                    count2++;
                }
            }

            return (count1 == 0 || count2 == 0) && (allowNeither || count1 + count2 > 0);
        }
    }
}
