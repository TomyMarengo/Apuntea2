package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.webapp.forms.UnlinkSubjectForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RemainingLinkedCareersValidator implements ConstraintValidator<RemainingLinkedCareers, UnlinkSubjectForm> {
    @Autowired
    private DataService dataService;

    @Override
    public void initialize(RemainingLinkedCareers remainingLinkedCareers) {
    }

    @Override
    public boolean isValid(UnlinkSubjectForm value, ConstraintValidatorContext context) {
        return dataService.countCareersBySubjectId(value.getSubjectId()) > 1;
    }
}
