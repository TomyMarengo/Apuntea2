package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.services.CareerService;
import ar.edu.itba.paw.services.InstitutionService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.dto.CareerDto;
import ar.edu.itba.paw.webapp.dto.InstitutionDto;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response listAllInstitutions() {
        final Collection<Institution> allInstitutions = institutionService.getInstitutions();
        final Collection<InstitutionDto> dtoInstitutions = allInstitutions
                .stream()
                .map(i -> InstitutionDto.fromInstitution(i, uriInfo))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<Collection<InstitutionDto>>(dtoInstitutions) {}).build();
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
    @Path("/{institutionId}/careers/{careerId}/subjects")
    @Produces(value = { MediaType.APPLICATION_JSON }) // TODO: Add versions
    public Response listAllSubjects(@PathParam("institutionId") final UUID institutionId, @PathParam("careerId") final UUID careerId) {
        final Collection<Subject> allSubjects = subjectService.getSubjects(careerId);
        final Collection<SubjectDto> dtoSubjects = allSubjects
                .stream()
                .map(s -> SubjectDto.fromSubject(s, uriInfo, careerId, institutionId))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<Collection<SubjectDto>>(dtoSubjects) {}).build();
    }

}
