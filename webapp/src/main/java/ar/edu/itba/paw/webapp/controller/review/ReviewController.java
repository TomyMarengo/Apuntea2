package ar.edu.itba.paw.webapp.controller.review;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
import ar.edu.itba.paw.models.exceptions.note.ReviewNotFoundException;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.forms.ReviewCreationForm;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.forms.ReviewUpdateForm;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.dto.DeleteReasonDto;
import ar.edu.itba.paw.webapp.forms.queries.ReviewQuery;
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
    @Path("/{noteId}_{userId}")
    @Produces(value = { ApunteaMediaType.REVIEW })
    public Response getReview(@PathParam("noteId") final UUID noteId, @PathParam("userId") final UUID userId) {
        final Review review = noteService.getReview(noteId, userId).orElseThrow(ReviewNotFoundException::new);
        final ReviewDto reviewRespDto = ReviewDto.fromReview(review, uriInfo);
        return Response.ok(reviewRespDto).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.REVIEW_COLLECTION })
    public Response listReviews(@Valid @BeanParam final ReviewQuery reviewQuery) {
        Page<Review> reviewPage;
        reviewPage = (reviewQuery.getTargetUser() != null)?
            noteService.getReviewsByTargetUser(
                    reviewQuery.getTargetUser(),
                    reviewQuery.getPage(),
                    reviewQuery.getPageSize()
            ) : noteService.getReviews(
                    reviewQuery.getNoteId(),
                    reviewQuery.getUserId(),
                    reviewQuery.getPage(),
                    reviewQuery.getPageSize()
            );

        final Collection<ReviewDto> reviewDtos = reviewPage.getContent()
                .stream()
                .map(d -> ReviewDto.fromReview(d, uriInfo))
                .collect(Collectors.toList());
        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<ReviewDto>>(reviewDtos) {}),
                uriInfo,
                reviewPage).build();
    }

    @POST
    @Consumes(value = { ApunteaMediaType.REVIEW })
    public Response createReview(@Valid final ReviewCreationForm reviewCreationForm) {
        final Review review = noteService.createReview(
                reviewCreationForm.getNoteId(),
                reviewCreationForm.getScore(),
                reviewCreationForm.getContent()
        );
        if (review == null)
            throw new ConflictResponseException("error.review.alreadyExists");
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.format("%s_%s", review.getNoteId(), review.getUserId())).build()).build();
    }

    @PATCH
    @Path("/{noteId}_{userId}")
    @Produces(value = { ApunteaMediaType.REVIEW })
    @PreAuthorize("@userPermissions.isCurrentUser(#userId)")
    public Response updateReview(@PathParam("noteId") final UUID noteId, @PathParam("userId") final UUID userId, @Valid final ReviewUpdateForm reviewUpdateForm) {
        noteService.updateReview(noteId, reviewUpdateForm.getScore(), reviewUpdateForm.getContent());
        return Response.noContent().build();
    }

    @POST
    @Path("/{noteId}_{userId}")
    @Consumes(value = { ApunteaMediaType.DELETE_REASON })
    @Secured("ROLE_ADMIN")
    public Response deleteReview(@PathParam("noteId") final UUID noteId, @PathParam("userId") final UUID userId, @Valid final DeleteReasonDto deleteReasonDto) {
        noteService.deleteReview(noteId, userId, deleteReasonDto.getReason());
        return Response.noContent().build();
    }

}
