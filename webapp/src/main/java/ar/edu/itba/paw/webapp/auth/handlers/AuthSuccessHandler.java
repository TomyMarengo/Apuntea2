package ar.edu.itba.paw.webapp.auth.handlers;

import ar.edu.itba.paw.webapp.auth.ApunteaUserDetails;
import ar.edu.itba.paw.webapp.auth.jwt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;

    @Autowired
    public AuthSuccessHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws MalformedURLException {
        if(authentication instanceof UsernamePasswordAuthenticationToken || (authentication instanceof JwtAuthToken && ((JwtTokenDetails) authentication.getDetails()).getTokenType().equals(JwtTokenType.REFRESH))) {
            ApunteaUserDetails userDetails = (ApunteaUserDetails) authentication.getPrincipal();
            response.addHeader("Access-Token", "Bearer " + jwtTokenService.createAccessToken(userDetails));
            response.addHeader("Refresh-Token", "Bearer " + jwtTokenService.createRefreshToken(userDetails));
        }
    }
}