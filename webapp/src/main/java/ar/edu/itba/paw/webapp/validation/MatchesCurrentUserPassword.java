package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MatchesCurrentUserPassword.MatchesCurrentUserPasswordValidator.class})
public @interface MatchesCurrentUserPassword {

    String message() default "validation.currentPasswordMismatch";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MatchesCurrentUserPassword[] matchesCurrentUserPassword();
    }

    class MatchesCurrentUserPasswordValidator implements ConstraintValidator<MatchesCurrentUserPassword, String> {
        @Autowired
        private SecurityService securityService;

        @Override
        public void initialize(MatchesCurrentUserPassword matchesCurrentUserPassword) {    }

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            if (s == null) return true;
            return securityService.currentUserPasswordMatches(s);
        }
    }

}

