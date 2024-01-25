package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.UUID;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingDirectory.ExistingDirectoryValidator.class})
public @interface ExistingDirectory {
    String message() default "{ar.edu.itba.paw.webapp.validation.ExistingDirectory.message}";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ExistingDirectory[] existingDirectory();
    }

    class ExistingDirectoryValidator implements ConstraintValidator<ExistingDirectory, UUID> {

        @Autowired
        private DirectoryService directoryService;

        @Override
        public void initialize(ExistingDirectory existingDirectory) {
        }

        @Override
        public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {
            return id != null &&  directoryService.getDirectoryById(id).isPresent();
        }
    }

}
