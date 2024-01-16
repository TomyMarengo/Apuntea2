package ar.edu.itba.paw.webapp.auth.handlers;

import ar.edu.itba.paw.webapp.auth.ApunteaUserDetails;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthToken;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenDetails;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenService;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    //TODO: Add again!!!!
    private final JwtTokenService jwtTokenService;

    @Autowired
    public AuthSuccessHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws MalformedURLException {
        if((!(authentication instanceof JwtAuthToken)) || ((JwtTokenDetails) authentication.getDetails()).getTokenType().equals(JwtTokenType.REFRESH)) {
            ApunteaUserDetails userDetails = (ApunteaUserDetails) authentication.getPrincipal();
            response.addHeader("Access-Token", "Bearer " + jwtTokenService.createAccessToken(userDetails));
            response.addHeader("Refresh-Token", "Bearer " + jwtTokenService.createRefreshToken(userDetails));
        }
    }
}