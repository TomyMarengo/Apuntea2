package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
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
@Constraint(validatedBy = {EmailValidator.class, UnusedEmail.UnusedEmailValidator.class})
public @interface UnusedEmail {
    String message() default "UnusedEmail.userForm.email";

    String regex() default RegexUtils.EMAIL_REGEX;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UnusedEmail[] unusedEmail();
    }

    class UnusedEmailValidator implements ConstraintValidator<UnusedEmail, String> {
        @Autowired
        private UserService userService;
        private Pattern pattern;

        @Override
        public void initialize(UnusedEmail constraintAnnotation) {
            pattern = Pattern.compile(constraintAnnotation.regex());
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value == null || value.isEmpty() || !pattern.matcher(value).matches() || !userService.findByEmail(value).isPresent();
        }
    }


}
