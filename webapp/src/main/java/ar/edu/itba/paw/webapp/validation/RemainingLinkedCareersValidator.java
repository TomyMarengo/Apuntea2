package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.CareerService;
import ar.edu.itba.paw.webapp.forms.UnlinkSubjectForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RemainingLinkedCareersValidator implements ConstraintValidator<RemainingLinkedCareers, UnlinkSubjectForm> {
    @Autowired
    private CareerService careerService;

    @Override
    public void initialize(RemainingLinkedCareers remainingLinkedCareers) {
    }

    public boolean isValid(UnlinkSubjectForm value, ConstraintValidatorContext context) {
        return careerService.countCareersBySubjectId(value.getSubjectId()) > 1;
    }
}
