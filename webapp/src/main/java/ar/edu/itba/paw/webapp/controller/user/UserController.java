package ar.edu.itba.paw.webapp.controller.user;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
import ar.edu.itba.paw.models.exceptions.FavoriteNotFoundException;
import ar.edu.itba.paw.models.exceptions.user.InvalidVerificationCodeException;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
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
    @Produces(value = { ApunteaMediaType.USER_V1 })
    public Response getUser(@PathParam("id") final UUID id) {
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        return Response.ok(new GenericEntity<UserResponseDto>(UserResponseDto.fromUser(user, uriInfo)){}).build();
    }

    @GET
    @Produces(value = {ApunteaMediaType.USER_COLLECTION_V1})
    public Response listUsers(@Valid @BeanParam final UserQuery userQuery) {
        final Page<User> userPage = (userQuery.getEmail() != null) ?
           Page.fromOptional(userService.findByEmail(userQuery.getEmail())) :
                userService.getUsers(
                    userQuery.getQuery(),
                    userQuery.getStatus(),
                    userQuery.getFollowedBy(),
                    userQuery.getPage(),
                    userQuery.getPageSize()
            );
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
    public Response createUser(@Valid final UserCreationDto userDto) {
        final UUID userId = userService.create(userDto.getEmail(), userDto.getPassword(), userDto.getCareerId(), Role.ROLE_STUDENT);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(userId.toString()).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @PreAuthorize("@userPermissions.isCurrentUser(#id)")
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA })
    public Response updateUser(@PathParam("id") final UUID id, @Valid @BeanParam final UserUpdateDto userDto) {
        userService.updateProfile(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(), userDto.getProfilePictureBytes(), userDto.getCareerId());
        if (userDto.getPassword() != null)
            userService.updateCurrentUserPassword(userDto.getPassword());
        if (userDto.getNotificationsEnabled() != null)
            userService.updateNotificationsEnabled(userDto.getNotificationsEnabled());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    @Secured("ROLE_ADMIN")
    @Consumes(value = { ApunteaMediaType.USER_UPDATE_STATUS_V1 })
    public Response updateUserStatus(@PathParam("id") final UUID id, @Valid final UserStatusDto userStatusDto) {
        if (userStatusDto.getUserStatus().equals(UserStatus.BANNED))
            userService.banUser(id, userStatusDto.getReason());
        else if (userStatusDto.getUserStatus().equals(UserStatus.ACTIVE))
            userService.unbanUser(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    @Secured("ROLE_ANONYMOUS")
    @Consumes(value = { ApunteaMediaType.USER_REQUEST_PASSWORD_CHANGE_V1 })
    public Response updateUserRequestPasswordChange(@PathParam("id") final UUID id) {
        verificationCodesService.sendForgotPasswordCode(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    @Secured("ROLE_ANONYMOUS")
    @Consumes(value = { ApunteaMediaType.USER_UPDATE_PASSWORD_V1 })
    public Response updateUserPassword(@PathParam("id") final UUID id, @Valid final PasswordForgotDto passwordForgotDto) {
        if (userService.updateUserPasswordWithCode(id, passwordForgotDto.getCode(), passwordForgotDto.getPassword()))
            return Response.noContent().build();
        throw new InvalidVerificationCodeException();
    }

    @GET
    @Path("/{id}/followers/{followerId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#followerId)")
    public Response isFollowing(@PathParam("id") final UUID id, @PathParam("followerId") final UUID followerId) {
        if (userService.isFollowing(id)) {
            return Response.noContent().build();
        }
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
