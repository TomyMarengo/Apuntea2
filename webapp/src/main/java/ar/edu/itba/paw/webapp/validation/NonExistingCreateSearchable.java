package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.SearchableCreationForm;
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
@Constraint(validatedBy = NonExistingCreateSearchable.NonExistingCreateSearchableValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonExistingCreateSearchable {

    String message() default "error.searchable.alreadyExists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NonExistingCreateSearchable[] nonExistingFile();
    }
    class NonExistingCreateSearchableValidator implements ConstraintValidator<NonExistingCreateSearchable, SearchableCreationForm> {
        @Autowired
        private SearchService searchService;

        @Override
        public void initialize(NonExistingCreateSearchable nonExistingSearchable) {
        }

        @Override
        public boolean isValid(SearchableCreationForm value, ConstraintValidatorContext context) {
            return !searchService.findByName(value.getParentId(), value.getName()).isPresent();
        }
    }
}
