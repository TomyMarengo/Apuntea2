package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.services.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class ExistingDirectoryValidator implements ConstraintValidator<ExistingDirectory, UUID> {

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
