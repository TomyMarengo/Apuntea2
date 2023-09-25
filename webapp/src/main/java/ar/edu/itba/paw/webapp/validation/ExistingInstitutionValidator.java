package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.Institution;
import ar.edu.itba.paw.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.UUID;


public class ExistingInstitutionValidator implements ConstraintValidator<ExistingInstitution, UUID> {
    @Autowired
    private DataService dataService;

    @Override
    public void initialize(ExistingInstitution constraintAnnotation) {
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        Optional<Institution> institution = dataService.findInstitutionById(value);
        return institution.isPresent();
    }
}
