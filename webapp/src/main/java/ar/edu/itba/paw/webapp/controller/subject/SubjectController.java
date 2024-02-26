package ar.edu.itba.paw.webapp.controller.subject;

import ar.edu.itba.paw.models.exceptions.institutional.SubjectNotFoundException;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectResponseDto;
import ar.edu.itba.paw.webapp.forms.RegexUtils;
import ar.edu.itba.paw.webapp.forms.queries.SubjectQuery;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    public Response listSubjects(@Valid @BeanParam final SubjectQuery subjectQuery){
        List<Subject> subjects = (subjectQuery.getCareerId() != null || subjectQuery.getUserId() != null )?
                subjectService.getSubjects(subjectQuery.getCareerId(), subjectQuery.getYear(), subjectQuery.getUserId()):
                subjectService.getSubjectsByCareerComplemented(subjectQuery.getNotInCareer());
        final Collection<SubjectResponseDto> subjectDtos = subjects.stream().map(s->SubjectResponseDto.fromSubject(s, uriInfo)).collect(java.util.stream.Collectors.toList());
        return Response.ok(new GenericEntity<Collection<SubjectResponseDto>>(subjectDtos){}).build();
    }

    @GET
    @Path("/{subjectId}")
    @Produces(value = {ApunteaMediaType.SUBJECT_V1 })
    public Response getSubject(@PathParam("subjectId") final UUID subjectId){
        Subject sub = subjectService.getSubject(subjectId).orElseThrow(SubjectNotFoundException::new);
        return Response.ok(SubjectResponseDto.fromSubject(sub, uriInfo)).build();
    }

    @POST
    @Consumes(value = { MediaType.TEXT_PLAIN })
    @Secured({"ROLE_ADMIN"})
    public Response createSubject(@Valid @NotNull @NotEmpty @Pattern(regexp = RegexUtils.FILE_REGEX) final String name){
        UUID subjectId = subjectService.createSubject(name);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(subjectId.toString()).build()).build();
    }

    @PATCH
    @Path("/{subjectId}")
    @Consumes(value = { MediaType.TEXT_PLAIN })
    @Secured({"ROLE_ADMIN"})
    public Response updateSubject(@PathParam("subjectId") final UUID subjectId, @Valid @NotNull @NotEmpty @Pattern(regexp = RegexUtils.FILE_REGEX) final String name) {
        subjectService.updateSubject(subjectId, name);
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
