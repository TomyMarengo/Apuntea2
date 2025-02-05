package ar.edu.itba.paw.webapp.controller.user;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
import ar.edu.itba.paw.models.exceptions.FavoriteNotFoundException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.UserStatus;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationCodesService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.user.dto.*;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.forms.queries.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("users")
@Component
public class UserController {
    private final SecurityService securityService;
    private final UserService userService;
    private final VerificationCodesService verificationCodesService;

    @Context
    private UriInfo uriInfo;


    @Autowired
    public UserController(final UserService userService, final SecurityService securityService, final VerificationCodesService verificationCodesService) {
        this.userService = userService;
        this.securityService = securityService;
        this.verificationCodesService = verificationCodesService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = { ApunteaMediaType.USER})
    public Response getUser(@PathParam("id") final UUID id) {
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    // TODO: Ask if this approach is correct
    @GET
    @Path("/{id}")
    @Secured("ROLE_ADMIN")
    @Produces(value = { ApunteaMediaType.USER_WITH_EMAIL})
    public Response getUserWithEmail(@PathParam("id") final UUID id) {
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserDto.fromUser(user, uriInfo, true)).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.USER_COLLECTION })
    public Response listUsers(@Valid @BeanParam final UserQuery userQuery) {
        final Page<User> userPage = (userQuery.getEmail() != null) ?
           Page.fromOptional(userService.findByEmail(userQuery.getEmail())) :
                userService.getUsers(
                    userQuery.getQuery(),
                    userQuery.getStatus(),
                    userQuery.getFollowedBy(),
                    userQuery.getFollowing(),
                    userQuery.getPage(),
                    userQuery.getPageSize()
            );
        final Collection<UserDto> dtoUsers = userPage.getContent()
                .stream()
                .map(u -> UserDto.fromUser(u, uriInfo))
                .collect(Collectors.toList());
        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<UserDto>>(dtoUsers) {}),
                uriInfo,
                userPage).build();
    }

    @GET
    @Secured("ROLE_ADMIN")
    @Produces(value = { ApunteaMediaType.USER_COLLECTION_WITH_EMAIL })
    public Response listUsersWithEmail(@Valid @BeanParam final UserQuery userQuery) {
        final Page<User> userPage = (userQuery.getEmail() != null) ?
                Page.fromOptional(userService.findByEmail(userQuery.getEmail())) :
                userService.getUsers(
                        userQuery.getQuery(),
                        userQuery.getStatus(),
                        userQuery.getFollowedBy(),
                        userQuery.getFollowing(),
                        userQuery.getPage(),
                        userQuery.getPageSize()
                );
        final Collection<UserDto> dtoUsers = userPage.getContent()
                .stream()
                .map(u -> UserDto.fromUser(u, uriInfo, true))
                .collect(Collectors.toList());
        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<UserDto>>(dtoUsers) {}),
                uriInfo,
                userPage).build();
    }


    @POST
    @Consumes(value = { ApunteaMediaType.USER })
    public Response createUser(@Valid final UserCreationForm userForm) {
        final UUID userId = userService.create(userForm.getEmail(), userForm.getPassword(), userForm.getCareerId(), Role.ROLE_STUDENT);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(userId.toString()).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @PreAuthorize("@userPermissions.isCurrentUser(#id)")
    @Consumes(value = { ApunteaMediaType.USER })
    public Response updateUser(@PathParam("id") final UUID id, @Valid final UserUpdateForm userForm) {
        userService.updateProfile(userForm.getFirstName(), userForm.getLastName(), userForm.getUsername(), userForm.getCareerId());
        if (userForm.getPassword() != null)
            userService.updateCurrentUserPassword(userForm.getPassword());
        if (userForm.getNotificationsEnabled() != null)
            userService.updateNotificationsEnabled(userForm.getNotificationsEnabled());
        return Response.noContent().build();
    }

    @POST
    @Secured("ROLE_ANONYMOUS")
    @Consumes(value = { ApunteaMediaType.USER_REQUEST_PASSWORD_CHANGE })
    public Response requestPasswordChange(@Valid final RequestPasswordChangeForm emailForm) {
        verificationCodesService.sendForgotPasswordCode(emailForm.getEmail());
        return Response.noContent().build();
    }

    @POST
    @Secured("ROLE_VERIFY")
    @Consumes(value = { ApunteaMediaType.USER_PASSWORD})
    public Response updateUserPassword(@Valid final UserPasswordUpdateForm userForm) {
        userService.updateCurrentUserPassword(userForm.getPassword());
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @Secured("ROLE_ADMIN")
    @Consumes(value = { ApunteaMediaType.USER_STATUS_REASON })
    public Response updateUserStatus(@PathParam("id") final UUID id, @Valid final UserStatusForm userStatusDto) {
        if (userStatusDto.getUserStatus().equals(UserStatus.BANNED))
            userService.banUser(id, userStatusDto.getReason());
        else if (userStatusDto.getUserStatus().equals(UserStatus.ACTIVE))
            userService.unbanUser(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/followers/{followerId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#followerId)")
    public Response isFollowing(@PathParam("id") final UUID id, @PathParam("followerId") final UUID followerId) {
        if (userService.isFollowing(id))
            return Response.noContent().build();
        throw new FavoriteNotFoundException();
    }

    @POST
    @Path("/{id}/followers")
    public Response follow(@PathParam("id") final UUID id){
        if (userService.follow(id)) {
            UUID userId = securityService.getCurrentUserOrThrow().getUserId();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(userId.toString()).build()).build();
        }
        throw new ConflictResponseException("error.follow.alreadyExists");
    }

    @DELETE
    @Path("/{id}/followers/{followerId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#followerId)")
    public Response unfollow(@PathParam("id") final UUID id, @PathParam("followerId") final UUID followerId) {
        userService.unfollow(id);
        return Response.noContent().build();
    }

}
