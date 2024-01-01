package ar.edu.itba.paw.webapp.validation;
import ar.edu.itba.paw.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchesCurrentUserPasswordValidator implements ConstraintValidator<MatchesCurrentUserPassword, String> {
    @Autowired
    private SecurityService securityService;

    @Override
    public void initialize(MatchesCurrentUserPassword matchesCurrentUserPassword) {    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return securityService.currentUserPasswordMatches(s);
    }
}
