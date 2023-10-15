package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.forms.institutional.UnlinkSubjectForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DetachableSubjectValidator implements ConstraintValidator<DetachableSubject, UnlinkSubjectForm> {
    @Autowired
    private SubjectService subjectService;

    @Override
    public void initialize(DetachableSubject detachableSubject) {
    }

    public boolean isValid(UnlinkSubjectForm value, ConstraintValidatorContext context) {
        return subjectService.isSubjectDetachable(value.getSubjectId());
    }
}
