package ar.edu.itba.paw.webapp.controller.user;

import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.controller.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.*;
import java.util.Optional;
import java.util.UUID;

@Path("pictures")
@Component
public class PictureController {
    private final UserService userService;

    @Context
    private UriInfo uriInfo;
    @Autowired
    public PictureController(final UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = { "image/jpeg", "image/png" })
    public Response getProfilePicture(@PathParam("id") final UUID id) {
        Optional<byte[]> maybeFile = userService.getProfilePictureById(id);
        if (!maybeFile.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        final Response.ResponseBuilder response = Response.ok(maybeFile.get());
        return CacheUtils.unconditionalCache(response).build();
    }
}
