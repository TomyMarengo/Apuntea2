package ar.edu.itba.paw.webapp.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("tokens")
@Component
public class TokenController {

    @Context
    private AuthenticationManager authenticationManager;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response createToken() {
        return Response.ok().build();
    }

}
