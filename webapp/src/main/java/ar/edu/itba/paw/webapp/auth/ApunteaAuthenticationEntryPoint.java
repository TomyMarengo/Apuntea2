package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApunteaAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // TODO: Improve
        ApiErrorDto apiErrorDto = new ApiErrorDto(authException.getMessage());
//
//        if (authException instanceof ApiErrorExceptionInt) {
//            apiErrorDto = ApiErrorDto.fromApiErrorException((ApiErrorExceptionInt) authException);
//        }else{
//            apiErrorDto = new ApiErrorDto(ApiErrorCode.FORBIDDEN, authException.getMessage());
//        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(ApunteaMediaType.ERROR_V1);

        mapper.writeValue(response.getWriter(), apiErrorDto);
    }
}
