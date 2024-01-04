package ar.edu.itba.paw.webapp.auth.handlers;

import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {

        // TODO: improve
        ApiErrorDto apiErrorDto = new ApiErrorDto(401, e.getMessage());

//        if (e instanceof ApiErrorExceptionInt) {
//            apiErrorDto = ApiErrorDto.fromApiErrorException((ApiErrorExceptionInt) e);
//        }else{
//            apiErrorDto = new ApiErrorDto(ApiErrorCode.ACCESS_DENIED, e.getMessage());
//        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(ApunteaMediaType.ERROR_V1);

        mapper.writeValue(response.getWriter(), apiErrorDto);
    }
}
