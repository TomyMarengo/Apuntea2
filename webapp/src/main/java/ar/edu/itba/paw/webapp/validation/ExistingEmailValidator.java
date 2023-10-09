package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ExistingEmailValidator implements ConstraintValidator<ExistingEmail, String> {

    @Autowired
    private UserService userService;
    private Pattern pattern;

    @Override
    public void initialize(ExistingEmail existingEmail) {
        pattern = Pattern.compile(existingEmail.regex());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && !s.isEmpty() && pattern.matcher(s).matches() && userService.findByEmail(s).isPresent();
    }
}
