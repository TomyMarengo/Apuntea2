package ar.edu.itba.paw.webapp.controller.institution;


import ar.edu.itba.paw.models.exceptions.institutional.CareerNotFoundException;
import ar.edu.itba.paw.models.exceptions.institutional.InstitutionNotFoundException;
import ar.edu.itba.paw.models.exceptions.institutional.SubjectCareerNotFoundException;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.SubjectCareer;
import ar.edu.itba.paw.services.CareerService;
import ar.edu.itba.paw.services.InstitutionService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.api.ApunteaMediaType;
import ar.edu.itba.paw.webapp.controller.institution.dtos.CareerDto;
import ar.edu.itba.paw.webapp.controller.institution.dtos.InstitutionDto;
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectCareerCreationDto;
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectCareerResponseDto;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("institutions")
@Component
public class InstitutionController {

    private final InstitutionService institutionService;
    private final CareerService careerService;
    private final SubjectService subjectService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public InstitutionController(final InstitutionService institutionService, final CareerService careerService, final SubjectService subjectService) {
        this.institutionService = institutionService;
        this.careerService = careerService;
        this.subjectService = subjectService;
    }


    @GET
    @Path("/{institutionId}")
    @Produces(value = { ApunteaMediaType.INSTITUTION_V1})
    public Response getInstitution(final @ValidUuid @PathParam("institutionId") UUID institutionId) {
        final Institution institution = institutionService.getInstitution(institutionId).orElseThrow(InstitutionNotFoundException::new);
        final InstitutionDto dtoInstitution = InstitutionDto.fromInstitution(institution, uriInfo);
        return Response.ok(new GenericEntity<InstitutionDto>(dtoInstitution) {}).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.INSTITUTION_COLLECTION_V1}) // TODO: Add versions
    public Response listAllInstitutions() {
        final Collection<Institution> allInstitutions = institutionService.getInstitutions();
        final Collection<InstitutionDto> dtoInstitutions = allInstitutions
                .stream()
                .map(i -> InstitutionDto.fromInstitution(i, uriInfo))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<Collection<InstitutionDto>>(dtoInstitutions) {}).build();
    }


    // TODO: Validate that the career belongs to the institution
    @GET
    @Path("/{institutionId}/careers/{careerId}")
    @Produces(value = { ApunteaMediaType.CAREER_V1})
    public Response getCareer(final @ValidUuid @PathParam("institutionId") UUID institutionId, final @ValidUuid @PathParam("careerId") UUID careerId) {
        final Career career = careerService.getCareerById(careerId).orElseThrow(CareerNotFoundException::new);
        final CareerDto dtoCareer = CareerDto.fromCareer(career, uriInfo, institutionId);
        return Response.ok(new GenericEntity<CareerDto>(dtoCareer) {}).build();
    }

    @GET
    @Path("/{institutionId}/careers")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response listAllCareers(@ValidUuid @PathParam("institutionId") final UUID institutionId) {
        final Collection<Career> allCareers = careerService.getCareers(institutionId);
        final Collection<CareerDto> dtoCareers = allCareers
                .stream()
                .map(c -> CareerDto.fromCareer(c, uriInfo, institutionId))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<Collection<CareerDto>>(dtoCareers) {}).build();
    }

    @GET
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response getSubjectCareer(@PathParam("institutionId") final UUID institutionId, @PathParam("careerId") final UUID careerId, @PathParam("subjectId") final UUID subjectId) {
        final SubjectCareer sc = subjectService.getSubjectCareer(careerId, subjectId).orElseThrow(SubjectCareerNotFoundException::new);
        final SubjectCareerResponseDto scDto = SubjectCareerResponseDto.fromSubjectCareer(sc, uriInfo);
        return Response.ok(new GenericEntity<SubjectCareerResponseDto>(scDto) {}).build();
    }

    @POST
    @Path("/{institutionId}/careers/{careerId}/subjectcareers")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response addSubjectCareer(@PathParam("institutionId") final UUID institutionId, @PathParam("careerId") final UUID careerId, @Valid @BeanParam SubjectCareerCreationDto scDto) {
        return Response.ok().build();
    }

    @PUT
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response updateSubjectCareer(@PathParam("institutionId") final UUID institutionId, @PathParam("careerId") final UUID careerId, @PathParam("subjectId") final UUID subjectId, @Valid @Range(min = 1, max = 10) final int year) {
        return Response.ok().build();
    }

    @DELETE
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response deleteSubjectCareer(@PathParam("institutionId") final UUID institutionId, @PathParam("careerId") final UUID careerId, @PathParam("subjectId") final UUID subjectId, @Valid @Min(1) @Max(10) final int year) {
        return Response.ok().build();
    }
}
