package ar.edu.itba.paw.webapp.controller.review;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
import ar.edu.itba.paw.models.exceptions.note.ReviewNotFoundException;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.note.dtos.NoteResponseDto;
import ar.edu.itba.paw.webapp.controller.review.dtos.ReviewCreationDto;
import ar.edu.itba.paw.webapp.controller.review.dtos.ReviewResponseDto;
import ar.edu.itba.paw.webapp.controller.review.dtos.ReviewUpdateDto;
import ar.edu.itba.paw.webapp.controller.utils.ControllerUtils;
import ar.edu.itba.paw.webapp.forms.queries.ReviewQuery;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Optional;
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
    @Produces(value = { ApunteaMediaType.REVIEW_V1 }) // TODO: Add versions
    public Response listReviews(@PathParam("noteId") final UUID noteId, @PathParam("userId") final UUID userId) {
        final Review review = noteService.getReview(noteId, userId).orElseThrow(ReviewNotFoundException::new);
        final ReviewResponseDto reviewRespDto = ReviewResponseDto.fromReview(review, uriInfo);
        return Response.ok(new GenericEntity<ReviewResponseDto>(reviewRespDto){}).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.REVIEW_COLLECTION_V1 }) // TODO: Add versions
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

        final Collection<ReviewResponseDto> reviewDtos = reviewPage.getContent()
                .stream()
                .map(d -> ReviewResponseDto.fromReview(d, uriInfo))
                .collect(Collectors.toList());
        return ControllerUtils.addPaginationLinks(Response.ok(new GenericEntity<Collection<ReviewResponseDto>>(reviewDtos) {}),
                uriInfo.getBaseUriBuilder().path("reviews"),
                reviewPage).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createReview(@Valid final ReviewCreationDto reviewCreationDto) {
        final Review review = noteService.createReview(
                reviewCreationDto.getNoteId(),
                reviewCreationDto.getScore(),
                reviewCreationDto.getContent()
        );
        if (review == null)
            throw new ConflictResponseException("error.review.alreadyExists");
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.format("%s_%s", review.getNoteId(), review.getUserId())).build()).build();
    }

    @PUT
    @Path("/{noteId}_{userId}")
    @Produces(value = { ApunteaMediaType.REVIEW_V1 }) // TODO: Add versions
    @PreAuthorize("@userPermissions.isCurrentUser(#userId)")
    public Response updateReview(@PathParam("noteId") final UUID noteId, @PathParam("userId") final UUID userId, @Valid final ReviewUpdateDto reviewUpdateDto) {
        final Review review = noteService.updateReview(
                noteId,
                reviewUpdateDto.getScore(),
                reviewUpdateDto.getContent()
        );
        final ReviewResponseDto reviewRespDto = ReviewResponseDto.fromReview(review, uriInfo);
        return Response.ok(new GenericEntity<ReviewResponseDto>(reviewRespDto){}).build();
    }

    @DELETE
    @Path("/{noteId}_{userId}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Secured("ROLE_ADMIN")
    public Response deleteReview(@PathParam("noteId") final UUID noteId, @PathParam("userId") final UUID userId, @Length(max = 255) final String reason) {
        noteService.deleteReview(noteId, userId, reason);
        return Response.noContent().build();
    }

}
