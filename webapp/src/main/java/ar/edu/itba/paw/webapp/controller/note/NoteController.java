package ar.edu.itba.paw.webapp.controller.note;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
import ar.edu.itba.paw.models.exceptions.FavoriteNotFoundException;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.dto.DeleteReasonDto;
import ar.edu.itba.paw.webapp.controller.utils.CacheUtils;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteCreationForm;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteDto;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteUpdateForm;
import ar.edu.itba.paw.webapp.forms.queries.NoteQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

@Path("notes")
@Component
public class NoteController {
    private final SecurityService securityService;
    private final NoteService noteService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public NoteController(final SecurityService securityService, final NoteService noteService) {
        this.noteService = noteService;
        this.securityService = securityService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = { ApunteaMediaType.NOTE })
    public Response getNote(@PathParam("id") final UUID id) {
        final Optional<Note> maybeNote = noteService.getNoteById(id);
        if (!maybeNote.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        final NoteDto noteDto = NoteDto.fromNote(maybeNote.get(), uriInfo);
        return Response.ok(noteDto).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.NOTE_COLLECTION })
    public Response listNotes(@Valid @BeanParam NoteQuery noteQuery) {
        final Page<Note> notePage = noteService.getNotes(
                noteQuery.getParentId(),
                noteQuery.getUserId(),
                noteQuery.getFavBy(),
                noteQuery.getCategory(),
                noteQuery.getWord(),
                noteQuery.getInstitutionId(),
                noteQuery.getCareerId(),
                noteQuery.getSubjectId(),
                noteQuery.getSortBy(),
                noteQuery.getAscending(),
                noteQuery.getPage(),
                noteQuery.getPageSize()
        );
        final Collection<NoteDto> noteDtos = notePage.getContent()
                .stream()
                .map(n -> NoteDto.fromNote(n, uriInfo))
                .collect(Collectors.toList());

        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<NoteDto>>(noteDtos) {}),
                uriInfo,
                notePage).build();
    }

    @POST
    @Consumes(value = { ApunteaMediaType.FORM_DATA })
    public  Response createNote(@Valid @NotNull(message = "error.body.empty") @BeanParam final NoteCreationForm noteForm) {
        final UUID noteId = noteService.createNote(
                noteForm.getName(),
                noteForm.getParentId(),
                noteForm.getVisible(),
                noteForm.getFile(),
                noteForm.getMimeType(),
                noteForm.getCategory()
        );
        return Response.created(uriInfo.getAbsolutePathBuilder().path(noteId.toString()).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(value = { ApunteaMediaType.NOTE })
    public Response updateNote(@PathParam("id") final UUID id, @Valid @NotNull(message = "error.body.empty") final NoteUpdateForm noteForm) {
        noteService.update(id, noteForm.getName(), noteForm.getVisible(), noteForm.getCategory());
        return Response.noContent().build();
    }

    // Delete with body is not standard according to RFC 9110
    @POST
    @Path("/{id}")
    @Consumes(value = { ApunteaMediaType.DELETE_REASON })
    @Secured("ROLE_ADMIN")
    public Response deleteNoteAdminPost(@PathParam("id") final UUID id, @Valid final DeleteReasonDto reasonDto) {
        noteService.delete(id, reasonDto.getReason());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteNote(@PathParam("id") final UUID id) {
        noteService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/file")
    public Response getNoteFile(@PathParam("id") final UUID id) {
        Optional<NoteFile> maybeFile = noteService.getNoteFileById(id);
        if (!maybeFile.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        final Response.ResponseBuilder response = Response.ok(maybeFile.get().getContent(), maybeFile.get().getMimeType());
        return CacheUtils.unconditionalCache(response).build();
    }

    @POST
    @Path("/{id}/favorites")
    public Response addFavorite(@PathParam("id") final UUID id){
        if (noteService.addFavorite(id)) {
            UUID userId = securityService.getCurrentUserOrThrow().getUserId();
            return Response.created(uriInfo.getAbsolutePathBuilder().path(userId.toString()).build()).build();
        }
        throw new ConflictResponseException("error.favorite.alreadyExists");
    }

    @GET
    @Path("/{id}/favorites/{userId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#userId)")
    public Response getFavorite(@PathParam("id") final UUID id, @PathParam("userId") final UUID userId) {
        if (noteService.isFavorite(id))
            return Response.noContent().build();
        throw new FavoriteNotFoundException();
    }

    @DELETE
    @Path("/{id}/favorites/{userId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#userId)")
    public Response deleteFavoriteNote(@PathParam("id") final UUID id, @PathParam("userId") final UUID userId) {
        noteService.removeFavorite(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/interactions/{userId}")
    @PreAuthorize("@userPermissions.isCurrentUser(#userId)")
    public Response addInteraction(@PathParam("id") final UUID id, @PathParam("userId") final UUID userId) {
        noteService.addInteraction(id);
        return Response.noContent().build();
    }
}