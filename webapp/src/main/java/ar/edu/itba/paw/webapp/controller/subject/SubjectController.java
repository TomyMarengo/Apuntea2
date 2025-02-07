package ar.edu.itba.paw.webapp.controller.subject;

import ar.edu.itba.paw.models.exceptions.institutional.SubjectNotFoundException;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.forms.SubjectForm;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.controller.utils.CacheUtils;
import ar.edu.itba.paw.webapp.forms.queries.SubjectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Path("subjects")
@Component
public class SubjectController {
    private final SubjectService subjectService;

    @Context
    private UriInfo uriInfo;
    public SubjectController(@Autowired SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GET
    @Produces(value = { ApunteaMediaType.SUBJECT_COLLECTION })
    public Response listSubjects(@Valid @BeanParam final SubjectQuery subjectQuery){
        final List<Subject> subjects = (subjectQuery.getCareerId() != null || subjectQuery.getUserId() != null )?
                subjectService.getSubjects(subjectQuery.getCareerId(), subjectQuery.getYear(), subjectQuery.getUserId()):
                subjectService.getSubjectsByCareerComplemented(subjectQuery.getNotInCareer());
        final Collection<SubjectDto> subjectDtos = subjects.stream().map(s-> SubjectDto.fromSubject(s, uriInfo)).collect(java.util.stream.Collectors.toList());
        return Response.ok(new GenericEntity<Collection<SubjectDto>>(subjectDtos){}).build();
    }

    @GET
    @Path("/{subjectId}")
    @Produces(value = { ApunteaMediaType.SUBJECT})
    public Response getSubject(@Context final Request request, @PathParam("subjectId") final UUID subjectId){
        final Subject sub = subjectService.getSubject(subjectId).orElseThrow(SubjectNotFoundException::new);
        return CacheUtils.conditionalCache(Response.ok(SubjectDto.fromSubject(sub, uriInfo)), request, sub.hashCode()).build();
    }

    @POST
    @Consumes(value = { ApunteaMediaType.SUBJECT })
    @Secured({"ROLE_ADMIN"})
    public Response createSubject(@Valid final SubjectForm subjectForm) {
        UUID subjectId = subjectService.createSubject(subjectForm.getName());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(subjectId.toString()).build()).build();
    }

    @PUT
    @Path("/{subjectId}")
    @Consumes(value = { ApunteaMediaType.SUBJECT })
    @Secured({"ROLE_ADMIN"})
    public Response updateSubject(@PathParam("subjectId") final UUID subjectId, @Valid SubjectForm subjectForm) {
        subjectService.updateSubject(subjectId, subjectForm.getName());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{subjectId}")
    @Secured({"ROLE_ADMIN"})
    public Response deleteSubject(@PathParam("subjectId") final UUID subjectId) {
        subjectService.deleteSubject(subjectId);
        return Response.noContent().build();
    }
}
