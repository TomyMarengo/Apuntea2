package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;
import java.util.UUID;

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
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response getUser(@PathParam("id") final UUID id) {
        final Optional<User> maybeUser = userService.findById(id);
        final Optional<UserDto> userDto = maybeUser.map(u -> UserDto.fromUser(u, uriInfo));
        if (!userDto.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(new GenericEntity<UserDto>(userDto.get()){}).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createUser(@Valid final UserDto userDto) {
        final UUID userId = userService.create(userDto.getEmail(), userDto.getPassword(), userDto.getCareerId(), Role.ROLE_STUDENT);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(userId.toString()).build()).build();
    }
}
