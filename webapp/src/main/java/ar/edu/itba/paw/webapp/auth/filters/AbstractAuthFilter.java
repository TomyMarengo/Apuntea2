package ar.edu.itba.paw.webapp.auth.filters;

import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationCodesService;
import ar.edu.itba.paw.webapp.auth.ApunteaUserDetails;
import ar.edu.itba.paw.webapp.auth.Credentials;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
public class AbstractAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static final int BASIC_LENGTH = 6;
    private static final int JWT_LENGTH = 7;

    private final VerificationCodesService verificationCodesService;
    private final UserService userService;

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    public AbstractAuthFilter(final UserService userService, final VerificationCodesService verificationCodesService) {
        super(new OrRequestMatcher(new AntPathRequestMatcher("/**")));
        this.verificationCodesService = verificationCodesService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null){
            return SecurityContextHolder.getContext().getAuthentication();
        }
        else if (authHeader.startsWith("Basic ")) {
            final Credentials credentials = getCredentialsFromBasic(authHeader);

            final Optional<Authentication> maybeVerificationCodeAuth = attemptCodeVerification(credentials);
            if (maybeVerificationCodeAuth.isPresent()) return maybeVerificationCodeAuth.get();

            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword());
            return getAuthenticationManager().authenticate(authenticationToken);
        }
        else if (authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring(JWT_LENGTH);
            return getAuthenticationManager().authenticate(new JwtAuthToken(authToken));
        }
        throw new InsufficientAuthenticationException("Unsupported Authorization header");
    }

    private Optional<Authentication> attemptCodeVerification(final Credentials credentials) {
        if (!verificationCodesService.verifyForgotPasswordCode(credentials.getEmail(), credentials.getPassword())) return Optional.empty();
        return userService.findByEmail(credentials.getEmail()).map(u -> {
            final Collection<GrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_VERIFY.getRole()));
            return new UsernamePasswordAuthenticationToken(new ApunteaUserDetails(u.getUserId(), u.getEmail(), u.getPassword(), roles), null, roles);
        });
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