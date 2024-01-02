package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.auth.ApunteaUserDetails;

import java.net.MalformedURLException;

public interface JwtTokenService {
    String createAccessToken(final ApunteaUserDetails userDetails) throws MalformedURLException;
    String createRefreshToken(final ApunteaUserDetails userDetails) throws MalformedURLException;
    JwtTokenDetails validateTokenAndGetDetails(final String token);
}