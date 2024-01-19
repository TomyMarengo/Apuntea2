package ar.edu.itba.paw.webapp.controller.subject;

import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectCreationDto;
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectResponseDto;
import ar.edu.itba.paw.webapp.forms.queries.SubjectQuery;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response listSubjects(@Valid @BeanParam SubjectQuery subjectQuery){
        List<Subject> subjects = (subjectQuery.getCareerId() != null)?
                subjectService.getSubjectsByCareer(subjectQuery.getCareerId(), subjectQuery.getYear()):
                subjectService.getSubjectsByCareerComplemented(subjectQuery.getNotInCareerId());
        final Collection<SubjectResponseDto> subjectDtos = subjects.stream().map(s->SubjectResponseDto.fromSubject(s, uriInfo)).collect(java.util.stream.Collectors.toList());
        return Response.ok(new GenericEntity<Collection<SubjectResponseDto>>(subjectDtos){}).build();
    }

    @GET
    @Path("/{subjectId}")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response getSubject(@PathParam("subjectId") final String subjectId){
        return Response.ok().build();
    }

    @POST
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA })
    public Response createSubject(@Valid @NotNull(message = "error.body.empty") @BeanParam final SubjectCreationDto subjectDto){
        return Response.ok().build();
    }

    @PATCH
    @Path("/{subjectId}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateSubject(@PathParam("subjectId") final UUID subjectId, @NotEmpty final String name) {
        return Response.ok().build();
    }

    @DELETE
    @Path("/{subjectId}")
    public Response deleteSubject(@PathParam("subjectId") final UUID subjectId) {
        return Response.ok().build();
    }
}
