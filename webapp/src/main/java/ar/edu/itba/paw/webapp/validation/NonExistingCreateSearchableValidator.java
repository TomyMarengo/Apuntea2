package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.SearchableCreationDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonExistingCreateSearchableValidator implements ConstraintValidator<NonExistingCreateSearchable, SearchableCreationDto> {
    @Autowired
    private SearchService searchService;

    @Override
    public void initialize(NonExistingCreateSearchable nonExistingSearchable) {
    }

    @Override
    public boolean isValid(SearchableCreationDto value, ConstraintValidatorContext context) {
        return !searchService.findByName(value.getParentId(), value.getName()).isPresent();
    }
}
