package ar.edu.itba.paw.webapp.controller.review;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteResponseDto;
import ar.edu.itba.paw.webapp.forms.queries.ReviewQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.stream.Collectors;

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
    @Produces(value = { ApunteaMediaType.REVIEW_COLLECTION_V1 }) // TODO: Add versions
    public Response listReviews(@Valid @BeanParam ReviewQuery reviewQuery) {
        Page<Review> reviewPage;
        if (reviewQuery.getDoneToUser() != null) {
            reviewPage = noteService.getReviewsDoneToUser(
                    reviewQuery.getDoneToUser(),
                    reviewQuery.getPage(),
                    reviewQuery.getPageSize()
            );
        } else {
            reviewPage = noteService.getReviews(
                    reviewQuery.getNoteId(),
                    reviewQuery.getPage(),
                    reviewQuery.getPageSize()
            );
        }

        final Collection<ReviewResponseDto> reviewDtos = reviewPage.getContent()
                .stream()
                .map(d -> ReviewResponseDto.fromReview(d, uriInfo))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<Collection<ReviewResponseDto>>(reviewDtos){}).build();
    }

}
