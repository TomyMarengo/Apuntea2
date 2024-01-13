package ar.edu.itba.paw.webapp.controller.directory;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.directory.dtos.DirectoryCreationDto;
import ar.edu.itba.paw.webapp.controller.directory.dtos.DirectoryUpdateDto;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.controller.directory.dtos.DirectoryResponseDto;
import ar.edu.itba.paw.webapp.forms.search.DirectoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final DirectoryService directoryService;
    @Context
    private UriInfo uriInfo;

    @Autowired
    public DirectoryController(final DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = { ApunteaMediaType.DIRECTORY_V1 }) // TODO: Add versions
    public Response getDirectory(@PathParam("id") final UUID id) {
        final Optional<Directory> maybeDirectory = directoryService.getDirectoryById(id);
        if (!maybeDirectory.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        final DirectoryResponseDto directoryDto = DirectoryResponseDto.fromDirectory(maybeDirectory.get(), uriInfo);
        return Response.ok(new GenericEntity<DirectoryResponseDto>(directoryDto){}).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.DIRECTORY_COLLECTION_V1 }) // TODO: Add versions
    public Response listDirectories(@Valid @BeanParam DirectoryQuery directoryQuery) {
        final Page<Directory> directoryPage = directoryService.getDirectories(
                directoryQuery.getParentId(),
                directoryQuery.getUserId(),
                directoryQuery.getFavBy(),
                directoryQuery.getWord(),
                directoryQuery.getSortBy(),
                directoryQuery.getAscending(),
                directoryQuery.getPageNumber(),
                directoryQuery.getPageSize()
        );
        final Collection<DirectoryResponseDto> dtoDirectories = directoryPage.getContent()
                .stream()
                .map(d -> DirectoryResponseDto.fromDirectory(d, uriInfo))
                .collect(Collectors.toList());

        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<DirectoryResponseDto>>(dtoDirectories) {}),
                uriInfo.getBaseUriBuilder().path("directories"),
                directoryPage).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createDirectory(@Valid  final DirectoryCreationDto directoryDto) {
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
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateDirectory(@PathParam("id") final UUID id, @Valid @NotNull(message = "error.body.empty") final DirectoryUpdateDto directoryDto) {
        directoryService.update(id, directoryDto.getName(), directoryDto.getVisible(), directoryDto.getIconColor());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response deleteDirectory(@PathParam("id") final UUID id, @QueryParam("reason") final String reason) {
        directoryService.delete(id, reason);
        return Response.noContent().build();
    }
}
