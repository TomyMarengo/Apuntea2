package ar.edu.itba.paw.webapp.controller.directory;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
import ar.edu.itba.paw.models.exceptions.FavoriteNotFoundException;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.directory.dtos.DirectoryCreationDto;
import ar.edu.itba.paw.webapp.controller.directory.dtos.DirectoryUpdateDto;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.controller.directory.dtos.DirectoryResponseDto;
import ar.edu.itba.paw.webapp.forms.queries.DirectoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("directories")
@Component
public class DirectoryController {
    private final SecurityService securityService;
    private final DirectoryService directoryService;
    @Context
    private UriInfo uriInfo;

    @Autowired
    public DirectoryController(final DirectoryService directoryService, final SecurityService securityService) {
        this.directoryService = directoryService;
        this.securityService = securityService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = { ApunteaMediaType.DIRECTORY })
    public Response getDirectory(@PathParam("id") final UUID id) {
        final Optional<Directory> maybeDirectory = directoryService.getDirectoryById(id);
        if (!maybeDirectory.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        final DirectoryResponseDto directoryDto = DirectoryResponseDto.fromDirectory(maybeDirectory.get(), uriInfo);
        return Response.ok(directoryDto).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.DIRECTORY_COLLECTION })
    public Response listDirectories(@Valid @BeanParam DirectoryQuery directoryQuery) {
        final Page<Directory> directoryPage = directoryService.getDirectories(
                directoryQuery.getParentId(),
                directoryQuery.getUserId(),
                directoryQuery.getFavBy(),
                directoryQuery.getWord(),
                directoryQuery.getInstitutionId(),
                directoryQuery.getCareerId(),
                directoryQuery.getSubjectId(),
                directoryQuery.isRdir(),
                directoryQuery.getSortBy(),
                directoryQuery.getAscending(),
                directoryQuery.getPage(),
                directoryQuery.getPageSize()
        );
        final Collection<DirectoryResponseDto> dtoDirectories = directoryPage.getContent()
                .stream()
                .map(d -> DirectoryResponseDto.fromDirectory(d, uriInfo))
                .collect(Collectors.toList());

        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<DirectoryResponseDto>>(dtoDirectories) {}),
                uriInfo,
                directoryPage).build();
    }

    @POST
    @Consumes(value = { ApunteaMediaType.DIRECTORY_CREATE })
    public Response createDirectory(@Valid final DirectoryCreationDto directoryDto) {
        final UUID DirectoryId = directoryService.create(
            directoryDto.getName(),
            directoryDto.getParentId(),
            directoryDto.getVisible(),
            directoryDto.getIconColor()
        );
        return Response.created(uriInfo.getAbsolutePathBuilder().path(DirectoryId.toString()).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(value = { ApunteaMediaType.DIRECTORY_UPDATE })
    public Response updateDirectory(@PathParam("id") final UUID id, @Valid @NotNull(message = "error.body.empty") final DirectoryUpdateDto directoryDto) {
        directoryService.update(id, directoryDto.getName(), directoryDto.getVisible(), directoryDto.getIconColor());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDirectory(@PathParam("id") final UUID id, @QueryParam("reason") final String reason) {
        directoryService.delete(id, reason);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/favorites/{followerId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#followerId)")
    public Response getFavorite(@PathParam("id") final UUID id, @PathParam("followerId") final UUID followerId) {
        if (directoryService.isFavorite(id))
            return Response.noContent().build();
        throw new FavoriteNotFoundException();
    }

    @POST
    @Path("/{id}/favorites")
    public Response addFavorite(@PathParam("id") final UUID id){
        if (directoryService.addFavorite(id)) {
            UUID userId = securityService.getCurrentUserOrThrow().getUserId();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(userId.toString()).build()).build();
        }
        throw new ConflictResponseException("error.favorite.alreadyExists");
    }

    @DELETE
    @Path("/{id}/favorites/{userId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#userId)")
    public Response deleteFavorite(@PathParam("id") final UUID id, @PathParam("userId") final UUID userId) {
        directoryService.removeFavorite(id);
        return Response.noContent().build();
    }
}

