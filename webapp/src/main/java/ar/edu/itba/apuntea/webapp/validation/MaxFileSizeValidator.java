package ar.edu.itba.apuntea.webapp.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.megabytes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // El archivo está vacío, por lo que la validación pasa
        }

        return file.getSize() / 1024 / 1024 <= maxSize;
    }
}