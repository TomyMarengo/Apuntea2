package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.forms.institutional.UnlinkSubjectForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DetachableSubject.DetachableSubjectValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DetachableSubject {
    String message() default "{ar.edu.itba.paw.webapp.validation.DetachableSubject.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        DetachableSubject[] detachableSubjects();
    }

    class DetachableSubjectValidator implements ConstraintValidator<DetachableSubject, UnlinkSubjectForm> {
        @Autowired
        private SubjectService subjectService;

        @Override
        public void initialize(DetachableSubject detachableSubject) {
        }

        public boolean isValid(UnlinkSubjectForm value, ConstraintValidatorContext context) {
            return subjectService.isSubjectDetachable(value.getSubjectId());
        }
    }

}
