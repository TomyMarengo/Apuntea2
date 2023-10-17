package ar.edu.itba.paw.webapp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthPageFilter extends GenericFilterBean {
    private	static final Logger LOGGER = LoggerFactory.getLogger(AuthPageFilter.class);

    private final String basePath;
    private final String[] subPaths;

    public AuthPageFilter(String basePath, String[] subPaths) {
        super();
        this.basePath = basePath;
        this.subPaths = new String[subPaths.length];
        for (int i = 0; i < subPaths.length; i++) {
            this.subPaths[i] = basePath + subPaths[i];
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && matchesSubpath(((HttpServletRequest)request).getRequestURI())) {
            LOGGER.debug("Redirecting authenticated user {} to index", auth.getName());
            ((HttpServletResponse)response).sendRedirect(basePath);
        }
        chain.doFilter(request, response);
    }

    private boolean matchesSubpath(String requestURI) {
        for (String subPath : subPaths) {
            if (requestURI.startsWith(subPath))
                return true;
        }
        return false;
    }

}
