package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UnusedUsernameValidator implements ConstraintValidator<UnusedUsername, String> {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    private Pattern pattern;


    @Override
    public void initialize(UnusedUsername constraintAnnotation) {
        pattern = Pattern.compile(constraintAnnotation.regex());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String currentUsername = this.securityService.getCurrentUser().map(User::getUsername).orElse("");
        return value == null || value.isEmpty() || !pattern.matcher(value).matches() || !userService.findByUsername(value)
                                                                                            .filter(u -> !u.getUsername().equals(currentUsername))
                                                                                            .isPresent();
    }
}

