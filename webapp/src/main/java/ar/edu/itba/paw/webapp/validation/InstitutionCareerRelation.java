package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.services.CareerService;
import ar.edu.itba.paw.services.InstitutionService;
import ar.edu.itba.paw.webapp.controller.institution.dtos.InstitutionCareerPathParams;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Optional;

@Documented
@Constraint(validatedBy = InstitutionCareerRelation.InstitutionCareerRelationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InstitutionCareerRelation {
    String message() default "institutionCareerMismatch";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class InstitutionCareerRelationValidator implements ConstraintValidator<InstitutionCareerRelation, InstitutionCareerPathParams> {
        @Autowired
        private CareerService careerService;

        @Override
        public void initialize(InstitutionCareerRelation constraintAnnotation) {}

        @Override
        public boolean isValid(InstitutionCareerPathParams form, ConstraintValidatorContext context) {
            if (form.getCareerId() == null || form.getInstitutionId() == null)
                return false;
            Optional<Career> maybeCareer = careerService.getCareerById(form.getCareerId());
            return maybeCareer.isPresent() && maybeCareer.get().getInstitutionId().equals(form.getInstitutionId());
        }
    }
}