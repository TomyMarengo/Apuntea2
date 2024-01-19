package ar.edu.itba.paw.webapp.auth.filters;

import ar.edu.itba.paw.webapp.auth.Credentials;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AbstractAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static final int BASIC_LENGTH = 6;
    private static final int JWT_LENGTH = 7;
    private static final int EMAIL_LENGTH = 6;


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    public AbstractAuthFilter() {
        super(new OrRequestMatcher(
                new AntPathRequestMatcher("/users", HttpMethod.GET),
                new AntPathRequestMatcher("/users/**", HttpMethod.PATCH),
                new AntPathRequestMatcher("/users/{id}/followers", HttpMethod.POST),
                new AntPathRequestMatcher("/users/{id}/followers/{userId}", HttpMethod.DELETE),
                new AntPathRequestMatcher("/notes/**", HttpMethod.POST),
                new AntPathRequestMatcher("/notes/**", HttpMethod.PATCH),
                new AntPathRequestMatcher("/notes/**", HttpMethod.DELETE),
                new AntPathRequestMatcher("/reviews", HttpMethod.POST),
                new AntPathRequestMatcher("/reviews/**", HttpMethod.PUT),
                new AntPathRequestMatcher("/reviews/**", HttpMethod.DELETE),
                new AntPathRequestMatcher("/directories/**", HttpMethod.POST),
                new AntPathRequestMatcher("/directories/**", HttpMethod.PATCH),
                new AntPathRequestMatcher("/directories/**", HttpMethod.DELETE),
                new AntPathRequestMatcher("/subjects/**"),
                new AntPathRequestMatcher("/tokens", HttpMethod.POST) // TODO: Remove
        ));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null){
            /* Nothing to be done */
        }
        else if(authHeader.startsWith("Basic ")){
            final Credentials credentials = getCredentialsFromBasic(authHeader);
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword());
            return getAuthenticationManager().authenticate(authenticationToken);
        }
        else if (authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring(JWT_LENGTH);
            return getAuthenticationManager().authenticate(new JwtAuthToken(authToken));
        }/*else if(authHeader.startsWith("Email ")){
            final String authToken = authHeader.substring(EMAIL_LENGTH);
            return getAuthenticationManager().authenticate(new EmailAuthToken(authToken));
        }*/
        // TODO: Remove comment?

        throw new InsufficientAuthenticationException("No authorization token provided");
    }

    // https://stackoverflow.com/questions/16000517/how-to-get-password-from-http-basic-authentication
    private Credentials getCredentialsFromBasic(String basic){
        // Authorization:Basic email:password
        String base64Credentials = basic.substring(BASIC_LENGTH).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        return new Credentials(values[0], values[1]);
    }
}