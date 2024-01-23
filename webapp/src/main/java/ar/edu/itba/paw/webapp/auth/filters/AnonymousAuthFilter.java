package ar.edu.itba.paw.webapp.auth.filters;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

@Component
public class AnonymousAuthFilter extends AnonymousAuthenticationFilter {
    public AnonymousAuthFilter() {
        super("anonymous");
    }

    public AnonymousAuthFilter(String key) {
        super(key);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            SecurityContextHolder.getContext().setAuthentication(createAuthentication(httpRequest));
        }
        chain.doFilter(request, response);
    }
}
