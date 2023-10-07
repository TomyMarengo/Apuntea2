package ar.edu.itba.paw.webapp.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFileNameValidator implements ConstraintValidator<ValidFileName, MultipartFile> {

    private String[] allowedExtensions;

    @Override
    public void initialize(ValidFileName constraintAnnotation) {
        allowedExtensions = constraintAnnotation.allowedExtensions();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            for (String extension : allowedExtensions) {
                if (originalFilename.toLowerCase().endsWith(extension.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }
}