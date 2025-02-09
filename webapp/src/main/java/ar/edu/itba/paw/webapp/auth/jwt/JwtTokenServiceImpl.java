package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.webapp.auth.ApunteaUserDetails;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidJwtClaimException;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidJwtTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenServiceImpl implements JwtTokenService {

    private final String jwtSecret;
    private final String jwtIssuer;

    @Autowired
    private Environment environment;

    private static final long ACCESS_TOKEN_DURATION_SECS = 10 * 60;  /* 10 min */
    private static final long REFRESH_TOKEN_DURATION_SECS = 30 * 24 * 60 * 60; /* 30 days */

    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String USER_ID_CLAIM = "userId";

    public JwtTokenServiceImpl(Environment environment) {
        this.jwtSecret = environment.getRequiredProperty("jwt.secret");
        this.jwtIssuer = environment.getRequiredProperty("jwt.issuer");
    }

    @Override
    public String createAccessToken(final ApunteaUserDetails userDetails) throws MalformedURLException {
        return createToken(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION_SECS * 1000), userDetails, JwtTokenType.ACCESS);
    }

    @Override
    public String createRefreshToken(final ApunteaUserDetails userDetails) throws MalformedURLException {
        return createToken(new Date(System.currentTimeMillis() + REFRESH_TOKEN_DURATION_SECS * 1000), userDetails, JwtTokenType.REFRESH);
    }

    private String createToken(final Date expiresAt, final ApunteaUserDetails userDetails, final JwtTokenType tokenType) throws MalformedURLException {
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        final JWTCreator.Builder token =  JWT.create()
                .withJWTId(generateTokenIdentifier())
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .withIssuer(jwtIssuer)
                .withClaim(AUTHORITIES_CLAIM, roles)
                .withClaim(TOKEN_TYPE_CLAIM, tokenType.getType())
                .withClaim(USER_ID_CLAIM, userDetails.getUserId().toString());

        return token.sign(Algorithm.HMAC256(jwtSecret.getBytes()));

    }


    @Override
    public JwtTokenDetails validateTokenAndGetDetails(final String token) {
        try {
            final DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtSecret.getBytes())).withIssuer(jwtIssuer).build().verify(token);
            return new JwtTokenDetails.Builder()
                    .withId(decodedJWT.getId())
                    .withEmail(decodedJWT.getSubject())
                    .withAuthorities(decodedJWT.getClaim(AUTHORITIES_CLAIM).asList(String.class))
                    .withIssuedDate(decodedJWT.getIssuedAt())
                    .withExpirationDate(decodedJWT.getExpiresAt())
                    .withToken(token)
                    .withTokenType(JwtTokenType.getByType(decodedJWT.getClaim(TOKEN_TYPE_CLAIM).asString()))
                    .build();
        }catch (InvalidClaimException e){
            throw new InvalidJwtClaimException(e.getMessage(), e);
        }catch (Exception e){
            throw new InvalidJwtTokenException(e.getMessage(), e);
        }
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}

