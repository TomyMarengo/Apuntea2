package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.CreateSearchableForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.UUID;

public class NonExistingCreateSearchableValidator implements ConstraintValidator<NonExistingCreateSearchable, CreateSearchableForm> {
    @Autowired
    private SearchService searchService;

    @Override
    public void initialize(NonExistingCreateSearchable nonExistingSearchable) {
    }

    @Override
    public boolean isValid(CreateSearchableForm value, ConstraintValidatorContext context) {
        return !searchService.findByName(value.getParentId(), value.getName()).isPresent();
    }
}
