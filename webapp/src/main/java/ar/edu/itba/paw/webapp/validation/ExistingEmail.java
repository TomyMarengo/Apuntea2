package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingEmail.ExistingEmailValidator.class})
public @interface ExistingEmail {
    String message() default "{ar.edu.itba.paw.webapp.validation.ExistingEmail.message}";

    String regex() default "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ExistingEmail[] existingEmail();
    }

    class ExistingEmailValidator implements ConstraintValidator<ExistingEmail, String> {

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
}
