package ar.edu.itba.paw.webapp.controller.note;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.utils.CacheUtils;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteCreationDto;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteResponseDto;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteUpdateDto;
import ar.edu.itba.paw.webapp.forms.search.NoteQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    private final NoteService noteService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public NoteController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = { ApunteaMediaType.NOTE_V1 }) // TODO: Add versions
    public Response getNote(@PathParam("id") final UUID id) {
        final Optional<Note> maybeNote = noteService.getNoteById(id);
        if (!maybeNote.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        final NoteResponseDto noteDto = NoteResponseDto.fromNote(maybeNote.get(), uriInfo);
        return Response.ok(new GenericEntity<NoteResponseDto>(noteDto){}).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.NOTE_COLLECTION_V1 }) // TODO: Add versions
    public Response listNotes(@Valid @BeanParam NoteQuery noteForm) {
        final Page<Note> notePage = noteService.getNotes(
                noteForm.getParentId(),
                noteForm.getUserId(),
                noteForm.getFavBy(),
                noteForm.getCategory(),
                noteForm.getWord(),
                noteForm.getSortBy(),
                noteForm.getAscending(),
                noteForm.getPageNumber(),
                noteForm.getPageSize()
        );
        final Collection<NoteResponseDto> noteDtos = notePage.getContent()
                .stream()
                .map(d -> NoteResponseDto.fromNote(d, uriInfo))
                .collect(Collectors.toList());

        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<NoteResponseDto>>(noteDtos) {}),
                uriInfo.getBaseUriBuilder().path("notes"),
                notePage).build();
    }

    @POST
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA })
    public  Response createNote(@Valid @NotNull(message = "error.body.empty") @BeanParam final NoteCreationDto noteDto) {
        final UUID noteId = noteService.createNote(
                noteDto.getName(),
                noteDto.getParentId(),
                noteDto.getVisible(),
                noteDto.getFile(),
                noteDto.getMimeType(),
                noteDto.getCategory()
        );
        return Response.created(uriInfo.getAbsolutePathBuilder().path(noteId.toString()).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateNote(@PathParam("id") final UUID id, @Valid @NotNull(message = "error.body.empty") final NoteUpdateDto noteDto) {
        noteService.update(id, noteDto.getName(), noteDto.getVisible(), noteDto.getCategory());
        return Response.noContent().build();
    }
    /*
    @DELETE
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response deleteNote(@PathParam("id") final UUID id, @QueryParam("reason") final String reason) {
        noteService.delete(id, reason);
        return Response.noContent().build();
    }*/

    @GET
    @Path("/{id}/file")
    @Produces
    public Response getNoteFile(@PathParam("id") final UUID id) {
        Optional<NoteFile> maybeFile = noteService.getNoteFileById(id);
        if (!maybeFile.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        final Response.ResponseBuilder response = Response.ok(maybeFile.get().getContent(), maybeFile.get().getMimeType());
        return CacheUtils.unconditionalCache(response).build();
    }
}
