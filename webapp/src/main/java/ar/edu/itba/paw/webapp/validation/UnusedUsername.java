package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {UnusedUsername.UnusedUsernameValidator.class})
public @interface UnusedUsername {
    String message() default "{ar.edu.itba.paw.webapp.validation.UnusedUsername.message}";

    String regex() default "^(?![-._]+$)(?!^\\d+$)[a-zA-Z0-9.\\-_]+$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UnusedUsername[] unusedUsername();
    }

    class UnusedUsernameValidator implements ConstraintValidator<UnusedUsername, String> {
        @Autowired
        private UserService userService;
        @Autowired
        private SecurityService securityService;
        private java.util.regex.Pattern pattern;


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

}
