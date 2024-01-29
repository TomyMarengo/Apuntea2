package ar.edu.itba.paw.webapp.auth.handlers;

import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import ar.edu.itba.paw.webapp.helpers.LocaleHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApunteaAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MessageSource messageSource;

    private	static final Logger LOGGER = LoggerFactory.getLogger(ApunteaAccessDeniedHandler.class);


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {

        LOGGER.error("{}: {}", e.getClass().getName(), e.getMessage());
        ApiErrorDto apiErrorDto = new ApiErrorDto(messageSource.getMessage("forbidden", null, LocaleHelper.getLocale()));

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(ApunteaMediaType.ERROR_V1);

        mapper.writeValue(response.getWriter(), apiErrorDto);
    }
}
