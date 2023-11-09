package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.webapp.forms.search.SortedSearchableForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidSortParamsValidator implements ConstraintValidator<ValidSortParams, SortedSearchableForm> {

    @Override
    public void initialize(ValidSortParams validSortParams) {

    }

    @Override
    public boolean isValid(SortedSearchableForm form, ConstraintValidatorContext constraintValidatorContext) {
        return form.getIsNote() || !form.getSortBy().equals("score");
    }
}
