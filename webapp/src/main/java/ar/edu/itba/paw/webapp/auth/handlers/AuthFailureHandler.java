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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MessageSource messageSource;

    private	static final Logger LOGGER = LoggerFactory.getLogger(AuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        LOGGER.error("{}: {}", e.getClass().getName(), e.getMessage());
        ApiErrorDto apiErrorDto = new ApiErrorDto(messageSource.getMessage("invalidCredentials", null, LocaleHelper.getLocale()));

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(ApunteaMediaType.ERROR);

        mapper.writeValue(response.getWriter(), apiErrorDto);
    }
}
