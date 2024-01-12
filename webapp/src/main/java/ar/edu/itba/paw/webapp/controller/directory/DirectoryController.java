package ar.edu.itba.paw.webapp.controller.directory;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.controller.directory.dtos.DirectoryDto;
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
        final DirectoryDto directoryDto = DirectoryDto.fromDirectory(maybeDirectory.get(), uriInfo);
        return Response.ok(new GenericEntity<DirectoryDto>(directoryDto){}).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.DIRECTORY_COLLECTION_V1 }) // TODO: Add versions
    public Response listDirectories(@Valid @BeanParam DirectoryQuery directoryForm) {
        final Page<Directory> directoryPage = directoryService.getDirectories(
                directoryForm.getParentId(),
                directoryForm.getUserId(),
                directoryForm.getFavBy(),
                directoryForm.getWord(),
                directoryForm.getSortBy(),
                directoryForm.getAscending(),
                directoryForm.getPageNumber(),
                directoryForm.getPageSize()
        );
        final Collection<DirectoryDto> dtoDirectories = directoryPage.getContent()
                .stream()
                .map(d -> DirectoryDto.fromDirectory(d, uriInfo))
                .collect(Collectors.toList());

        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<DirectoryDto>>(dtoDirectories) {}),
                uriInfo.getBaseUriBuilder().path("directories"),
                directoryPage).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createDirectory(@Valid  final DirectoryDto directoryDto) {
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
    public Response updateDirectory(@PathParam("id") final UUID id, @Valid @NotNull(message = "error.body.empty") final DirectoryDto directoryDto) {
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
