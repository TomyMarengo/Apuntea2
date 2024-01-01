package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.webapp.forms.RegexUtils;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EmailValidator.class, UnusedEmailValidator.class})
public @interface UnusedEmail {
    String message() default "{ar.edu.itba.paw.webapp.validation.UnusedEmail.message}";

    String regex() default RegexUtils.EMAIL_REGEX;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UnusedEmail[] unusedEmail();
    }
}
