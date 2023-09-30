package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.EditSearchableForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class NonExistingEditSearchableValidator implements ConstraintValidator<NonExistingEditSearchable, EditSearchableForm> {
    @Autowired
    private SearchService searchService;

    @Override
    public void initialize(NonExistingEditSearchable nonExistingEditSearchable) {

    }

    @Override
    public boolean isValid(EditSearchableForm editSearchableForm, ConstraintValidatorContext constraintValidatorContext) {
       UUID searchableId = searchService.findByName(editSearchableForm.getParentId(), editSearchableForm.getName()).orElse(null);
       return searchableId == null || searchableId.equals(editSearchableForm.getId());

    }
}
