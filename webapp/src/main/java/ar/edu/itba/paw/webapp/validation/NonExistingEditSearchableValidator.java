package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.SearchableUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class NonExistingEditSearchableValidator implements ConstraintValidator<NonExistingEditSearchable, SearchableUpdateDto> {
    @Autowired
    private SearchService searchService;

    @Override
    public void initialize(NonExistingEditSearchable nonExistingEditSearchable) {

    }

    @Override
    public boolean isValid(SearchableUpdateDto searchableUpdateDto, ConstraintValidatorContext constraintValidatorContext) {
       UUID searchableId = searchService.findByName(searchableUpdateDto.getParentId(), searchableUpdateDto.getName()).orElse(null);
       return searchableId == null || searchableId.equals(searchableUpdateDto.getId());

    }
}
