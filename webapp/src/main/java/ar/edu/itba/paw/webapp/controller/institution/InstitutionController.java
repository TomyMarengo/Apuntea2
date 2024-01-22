package ar.edu.itba.paw.webapp.controller.institution;


import ar.edu.itba.paw.models.exceptions.ConflictResponseException;
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
import ar.edu.itba.paw.webapp.controller.institution.dtos.InstitutionCareerPathParams;
import ar.edu.itba.paw.webapp.controller.institution.dtos.InstitutionDto;
import ar.edu.itba.paw.webapp.controller.review.dtos.ReviewResponseDto;
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
    public Response getInstitution(@ValidUuid @PathParam("institutionId") final UUID institutionId) {
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
    public Response getCareer(@Valid @BeanParam final InstitutionCareerPathParams instCarParams) {
        final Career career = careerService.getCareerById(instCarParams.getCareerId()).orElseThrow(CareerNotFoundException::new);
        final CareerDto dtoCareer = CareerDto.fromCareer(career, uriInfo);
        return Response.ok(new GenericEntity<CareerDto>(dtoCareer) {}).build();
    }

    @GET
    @Path("/{institutionId}/careers")
    @Produces(value = { ApunteaMediaType.CAREER_COLLECTION_V1 })
    public Response listAllCareers(@ValidUuid @PathParam("institutionId") final UUID institutionId) {
        final Collection<Career> allCareers = careerService.getCareers(institutionId);
        final Collection<CareerDto> dtoCareers = allCareers
                .stream()
                .map(c -> CareerDto.fromCareer(c, uriInfo))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<Collection<CareerDto>>(dtoCareers) {}).build();
    }

    @GET
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response getSubjectCareer(final @Valid @BeanParam InstitutionCareerPathParams instCarParams, @PathParam("subjectId") final UUID subjectId) {
        final SubjectCareer sc = subjectService.getSubjectCareer(instCarParams.getCareerId(), subjectId).orElseThrow(SubjectCareerNotFoundException::new);
        final SubjectCareerResponseDto scDto = SubjectCareerResponseDto.fromSubjectCareer(sc, uriInfo);
        return Response.ok(new GenericEntity<SubjectCareerResponseDto>(scDto) {}).build();
    }

    @POST
    @Path("/{institutionId}/careers/{careerId}/subjectcareers")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response addSubjectCareer(@Valid @BeanParam final InstitutionCareerPathParams instCarParams, @Valid @BeanParam final SubjectCareerCreationDto scDto) {
        if (subjectService.linkSubjectToCareer(scDto.getSubjectId(), instCarParams.getCareerId(), scDto.getYear()))
            return Response.created(uriInfo.getAbsolutePathBuilder().path(instCarParams.getInstitutionId().toString()).path("careers").path(instCarParams.getCareerId().toString()).path("subjectcareers").path(scDto.getSubjectId().toString()).build()).build();
        throw new ConflictResponseException("error.subjectcareer.alreadyExists");
    }

    @PUT
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response updateSubjectCareer(@Valid @BeanParam final InstitutionCareerPathParams instCarParams, @PathParam("subjectId") final UUID subjectId, @Valid @Range(min = 1, max = 10) final int year) {
        subjectService.updateSubjectCareer(subjectId, instCarParams.getCareerId(), year);
        return Response.ok(new GenericEntity<SubjectCareerResponseDto>(
                new SubjectCareerResponseDto(year, instCarParams.getInstitutionId(), instCarParams.getCareerId(), subjectId, uriInfo)){}
        ).build();
    }

    @DELETE
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    public Response deleteSubjectCareer(@Valid @BeanParam final InstitutionCareerPathParams instCarParams, @PathParam("subjectId") final UUID subjectId) {
        if (subjectService.unlinkSubjectFromCareer(instCarParams.getCareerId(), subjectId))
            return Response.noContent().build();
        throw new ConflictResponseException("error.favorite.alreadyExists");
    }
}
