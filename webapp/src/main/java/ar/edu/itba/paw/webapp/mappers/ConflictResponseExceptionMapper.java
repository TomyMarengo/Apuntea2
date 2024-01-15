package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
import ar.edu.itba.paw.models.exceptions.InvalidQueryException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import ar.edu.itba.paw.webapp.helpers.LocaleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ConflictResponseExceptionMapper implements ExceptionMapper<ConflictResponseException> {

    private	static final Logger LOGGER = LoggerFactory.getLogger(ConflictResponseExceptionMapper.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(ConflictResponseException e) {
        LOGGER.error("{}: {}", e.getClass().getName(), e.getMessage());
        ApiErrorDto errorDto = new ApiErrorDto(messageSource.getMessage(e.getMessage(), null, LocaleHelper.getLocale()));
        return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(errorDto).build();
    }
}
