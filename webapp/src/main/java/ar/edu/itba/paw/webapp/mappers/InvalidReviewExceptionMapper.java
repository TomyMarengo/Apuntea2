package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import ar.edu.itba.paw.webapp.helpers.LocaleHelper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class InvalidReviewExceptionMapper implements ExceptionMapper<InvalidReviewException> {
    private	static final Logger LOGGER = LoggerFactory.getLogger(InvalidReviewExceptionMapper.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(InvalidReviewException e) {
        LOGGER.error("{}: {}", e.getClass().getName(), e.getMessage());
        ApiErrorDto errorDto = new ApiErrorDto(messageSource.getMessage("error.review.invalid", null, LocaleHelper.getLocale()));
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(errorDto).build();
    }
}
