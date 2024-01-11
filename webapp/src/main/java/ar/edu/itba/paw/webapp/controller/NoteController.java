package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.dto.NoteDto;
import ar.edu.itba.paw.webapp.forms.search.NoteForm;
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
        final NoteDto noteDto = NoteDto.fromNote(maybeNote.get(), uriInfo);
        return Response.ok(new GenericEntity<NoteDto>(noteDto){}).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.NOTE_COLLECTION_V1 }) // TODO: Add versions
    public Response listNotes(@Valid @BeanParam NoteForm noteForm) {
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
        final Collection<NoteDto> noteDtos = notePage.getContent()
                .stream()
                .map(d -> NoteDto.fromNote(d, uriInfo))
                .collect(Collectors.toList());

        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<NoteDto>>(noteDtos) {}),
                uriInfo.getBaseUriBuilder().path("notes"),
                notePage).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateDirectory(@PathParam("id") final UUID id, @Valid @NotNull(message = "error.body.empty") final NoteDto noteDto) {
        noteService.update(id, noteDto.getName(), noteDto.getVisible(), noteDto.getCategory());
        return Response.noContent().build();
    }
    /*
    @DELETE
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response deleteDirectory(@PathParam("id") final UUID id, @QueryParam("reason") final String reason) {
        directoryService.delete(id, reason);
        return Response.noContent().build();
    }*/
}
