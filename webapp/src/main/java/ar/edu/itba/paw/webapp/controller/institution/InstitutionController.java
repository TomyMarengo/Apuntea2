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
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectCareerCreationForm;
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectCareerDto;
import ar.edu.itba.paw.webapp.controller.subject.dtos.SubjectCareerUpdateForm;
import ar.edu.itba.paw.webapp.controller.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
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
    @Produces(value = { ApunteaMediaType.INSTITUTION })
    public Response getInstitution(@Context final Request request, @PathParam("institutionId") final UUID institutionId) {
        final Institution institution = institutionService.getInstitution(institutionId).orElseThrow(InstitutionNotFoundException::new);
        final InstitutionDto dtoInstitution = InstitutionDto.fromInstitution(institution, uriInfo);
        return CacheUtils.conditionalCache(Response.ok(dtoInstitution), request, institution.hashCode()).build();
    }

    @GET
    @Produces(value = { ApunteaMediaType.INSTITUTION_COLLECTION })
    public Response listAllInstitutions(@Context final Request request) {
        final Collection<Institution> allInstitutions = institutionService.getInstitutions();
        final Collection<InstitutionDto> dtoInstitutions = allInstitutions
                .stream()
                .map(i -> InstitutionDto.fromInstitution(i, uriInfo))
                .collect(Collectors.toList());
        return CacheUtils.conditionalCache(
                Response.ok(new GenericEntity<Collection<InstitutionDto>>(dtoInstitutions) {}),
                request,
                allInstitutions.hashCode()
        ).build();
    }

    @GET
    @Path("/{institutionId}/careers/{careerId}")
    @Produces(value = { ApunteaMediaType.CAREER })
    public Response getCareer(@Context final Request request, @Valid @BeanParam final InstitutionCareerPathParams instCarParams) {
        final Career career = careerService.getCareerById(instCarParams.getCareerId()).orElseThrow(CareerNotFoundException::new);
        final CareerDto dtoCareer = CareerDto.fromCareer(career, uriInfo);
        return CacheUtils.conditionalCache(Response.ok(dtoCareer), request, career.hashCode()).build();
    }

    @GET
    @Path("/{institutionId}/careers")
    @Produces(value = { ApunteaMediaType.CAREER_COLLECTION })
    public Response listAllCareers(@Context Request request, @PathParam("institutionId") final UUID institutionId) {
        final Collection<Career> allCareers = careerService.getCareers(institutionId);
        final Collection<CareerDto> dtoCareers = allCareers
                .stream()
                .map(c -> CareerDto.fromCareer(c, uriInfo))
                .collect(Collectors.toList());
        return CacheUtils.conditionalCache(Response.ok(new GenericEntity<Collection<CareerDto>>(dtoCareers) {}), request, allCareers.hashCode()).build();
    }

    @GET
    @Path("/{institutionId}/careers/{careerId}/subjectcareers")
    @Produces(value = { ApunteaMediaType.SUBJECT_CAREER_COLLECTION })
    public Response listAllSubjectCareers(@Valid @BeanParam InstitutionCareerPathParams instCarParams) {
        final Collection<SubjectCareer> allSubjectCareers = subjectService.getSubjectCareers(instCarParams.getCareerId());
        final Collection<SubjectCareerDto> dtoSubjectCareers = allSubjectCareers
                .stream()
                .map(sc -> SubjectCareerDto.fromSubjectCareer(sc, uriInfo))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<Collection<SubjectCareerDto>>(dtoSubjectCareers) {}).build();
    }

    @GET
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Produces(value = { ApunteaMediaType.SUBJECT_CAREER })
    public Response getSubjectCareer(final @Valid @BeanParam InstitutionCareerPathParams instCarParams, @PathParam("subjectId") final UUID subjectId) {
        final SubjectCareer sc = subjectService.getSubjectCareer(subjectId, instCarParams.getCareerId()).orElseThrow(SubjectCareerNotFoundException::new);
        final SubjectCareerDto scDto = SubjectCareerDto.fromSubjectCareer(sc, uriInfo);
        return Response.ok(scDto).build();
    }

    @POST
    @Path("/{institutionId}/careers/{careerId}/subjectcareers")
    @Consumes(value = { ApunteaMediaType.SUBJECT_CAREER })
    @Secured({"ROLE_ADMIN"})
    public Response addSubjectCareer(@Valid @BeanParam final InstitutionCareerPathParams instCarParams, @Valid final SubjectCareerCreationForm scForm) {
        if (subjectService.linkSubjectToCareer(scForm.getSubjectId(), instCarParams.getCareerId(), scForm.getYear()))
            return Response.created(uriInfo.getAbsolutePathBuilder().path(scForm.getSubjectId().toString()).build()).build();
        throw new ConflictResponseException("error.subjectcareer.alreadyExists");
    }

    @PUT
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Consumes(value = { ApunteaMediaType.SUBJECT_CAREER })
    @Produces(value = { ApunteaMediaType.SUBJECT_CAREER })
    @Secured({"ROLE_ADMIN"})
    public Response updateSubjectCareer(@Valid @BeanParam final InstitutionCareerPathParams instCarParams, @PathParam("subjectId") final UUID subjectId, @Valid final SubjectCareerUpdateForm subjectCareerForm) {
        subjectService.updateSubjectCareer(subjectId, instCarParams.getCareerId(), subjectCareerForm.getYear());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{institutionId}/careers/{careerId}/subjectcareers/{subjectId}")
    @Secured({"ROLE_ADMIN"})
    public Response deleteSubjectCareer(@Valid @BeanParam final InstitutionCareerPathParams instCarParams, @PathParam("subjectId") final UUID subjectId) {
        subjectService.unlinkSubjectFromCareer(subjectId, instCarParams.getCareerId());
        return Response.noContent().build();
    }
}
