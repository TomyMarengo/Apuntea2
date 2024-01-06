package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    private final UserService userService;

    @Context
    private UriInfo uriInfo;


    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{email}")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response getUser(@PathParam("email") final String email) {
        final Optional<User> maybeUser = userService.findByEmail(email);
        final UserDto userDto = maybeUser.map(u -> UserDto.fromUser(u, uriInfo)).orElseThrow(UserNotFoundException::new);
        return Response.ok(new GenericEntity<UserDto>(userDto){}).build();
    }


}
