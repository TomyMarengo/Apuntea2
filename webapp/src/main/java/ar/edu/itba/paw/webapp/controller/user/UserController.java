package ar.edu.itba.paw.webapp.controller.user;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.controller.user.dto.UserCreationDto;
import ar.edu.itba.paw.webapp.controller.user.dto.UserResponseDto;
import ar.edu.itba.paw.webapp.controller.user.dto.UserUpdateDto;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        final Optional<UserResponseDto> userDto = maybeUser.map(u -> UserResponseDto.fromUser(u, uriInfo));
        if (!userDto.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(new GenericEntity<UserResponseDto>(userDto.get()){}).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions and validate
    public Response listUsers(@QueryParam("query") String query,
                              @QueryParam("status") String status,
                              @QueryParam("page") @DefaultValue("1") int page) {
        final Page<User> userPage = userService.getStudents(query, status, page);
        final Collection<UserResponseDto> dtoUsers = userPage.getContent()
                .stream()
                .map(u -> UserResponseDto.fromUser(u, uriInfo))
                .collect(Collectors.toList());
        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<UserResponseDto>>(dtoUsers) {}),
                uriInfo.getBaseUriBuilder().path("users"),
                userPage).build();
    }


    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createUser(@Valid @BeanParam final UserCreationDto userDto) {
        final UUID userId = userService.create(userDto.getEmail(), userDto.getPassword(), userDto.getCareerId(), Role.ROLE_STUDENT);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(userId.toString()).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @PreAuthorize("@userPermissions.isCurrentUser(#id)")
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA })
    public Response updateUser(@PathParam("id") final UUID id, @Valid @BeanParam final UserUpdateDto userDto) {
        userService.updateProfile(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(), userDto.getProfilePictureBytes(), userDto.getCareerId());
        // TODO: Profile picture
        if (userDto.getPassword() != null)
            userService.updateCurrentUserPassword(userDto.getPassword());
        if (userDto.getNotificationsEnabled() != null)
            userService.updateNotificationsEnabled(userDto.getNotificationsEnabled());
        return Response.noContent().build();
    }

}
