package ar.edu.itba.paw.webapp.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxFileSize.MaxFileSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSize {
    String message() default "{ar.edu.itba.paw.webapp.validation.MaxFileSize.message}";
    long megabytes(); // Maximum value in megabytes
    boolean allowEmptyFiles(); // If true, empty files can be uploaded
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {
        private long maxSize;
        private boolean allowEmptyFiles;

        @Override
        public void initialize(MaxFileSize constraintAnnotation) {
            this.maxSize = constraintAnnotation.megabytes();
            this.allowEmptyFiles = constraintAnnotation.allowEmptyFiles();
        }

        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
            if (file == null || file.isEmpty()) {
                return allowEmptyFiles;
            }

            return file.getSize() / 1024 / 1024 <= maxSize;
        }
    }
}