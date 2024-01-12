package ar.edu.itba.paw.webapp.controller.review;

import ar.edu.itba.paw.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("reviews")
@Component
public class ReviewController {

    private final NoteService noteService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public ReviewController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @GET
    public Response getReviews() {
        // TODO
        return Response.ok().build();
    }

}
