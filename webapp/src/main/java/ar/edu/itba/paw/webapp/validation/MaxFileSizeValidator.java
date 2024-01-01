package ar.edu.itba.paw.webapp.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {
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