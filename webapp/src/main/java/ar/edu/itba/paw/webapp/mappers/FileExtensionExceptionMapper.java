package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.FileExtensionException;
import ar.edu.itba.paw.models.exceptions.InvalidException;
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
import java.util.Arrays;

@Provider
@Component
public class FileExtensionExceptionMapper implements ExceptionMapper<FileExtensionException> {
    private	static final Logger LOGGER = LoggerFactory.getLogger(FileExtensionExceptionMapper.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(FileExtensionException e) {
        LOGGER.error("{}: {}", e.getClass().getName(), e.getMessage());
        final ApiErrorDto errorDto = new ApiErrorDto(messageSource.getMessage("error.file.extension", new String[] { String.join(", ", e.getAllowedExtensions()) }, LocaleHelper.getLocale()));
        return Response.status(422).type(MediaType.APPLICATION_JSON).entity(errorDto).build();
    }
}
