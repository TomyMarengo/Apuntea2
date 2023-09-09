package ar.edu.itba.apuntea.webapp.validation;

import com.sun.xml.internal.ws.api.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
            return false;
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