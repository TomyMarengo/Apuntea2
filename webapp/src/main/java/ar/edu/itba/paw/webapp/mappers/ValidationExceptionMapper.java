package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;
import ar.edu.itba.paw.webapp.helpers.LocaleHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Provider
@Component
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        final List<ValidationErrorDto> errorList = e.getConstraintViolations()
                .stream()
                .map(ValidationErrorDto::fromValidationException)
                .peek(err -> err.setMessage(
                        messageSource.getMessage(err.getMessage(), new Object[] {err.getField()}, err.getMessage(), LocaleHelper.getLocale())
                ))
                .collect(Collectors.toList());

        Optional<ValidationErrorDto> maybeNotFound = errorList.stream().filter(ValidationErrorDto::isNotFound).findFirst();
        if (maybeNotFound.isPresent())
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new GenericEntity<ValidationErrorDto>(maybeNotFound.get()) {}).build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<List<ValidationErrorDto>>(errorList) {}).build();
    }
}
