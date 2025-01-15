package ar.edu.itba.paw.webapp.controller.institution.dtos;

import ar.edu.itba.paw.models.institutional.Career;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;


public class CareerResponseDto {
    private UUID id;
    private String name;
    private URI self;
    private URI institution;
    private URI subjects;
    private URI subjectsNotInCareer;

    private URI subjectCareers;


    public static CareerResponseDto fromCareer(final Career career, final UriInfo uriInfo){
        final CareerResponseDto careerDto = new CareerResponseDto();
        careerDto.id = career.getCareerId();
        careerDto.name = career.getName();
        UriBuilder builder = uriInfo.getBaseUriBuilder();

        careerDto.institution = builder.path("institutions").path(career.getInstitutionId().toString()).build();
        careerDto.self = builder.path("careers").path(career.getCareerId().toString()).build();
        careerDto.subjectCareers = builder.path("subjectcareers").build();
        careerDto.subjects = uriInfo.getBaseUriBuilder().path("subjects").queryParam("careerId", career.getCareerId().toString()).build();
        careerDto.subjectsNotInCareer = uriInfo.getBaseUriBuilder().path("subjects").queryParam("notInCareer", career.getCareerId().toString()).build();
        return careerDto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public URI getSelf(){
        return self;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSelf(URI self){
        this.self = self;
    }

    public URI getSubjects() {
        return subjects;
    }

    public void setSubjects(URI subjects) {
        this.subjects = subjects;
    }

    public URI getInstitution() {
        return institution;
    }

    public void setInstitution(URI institution) {
        this.institution = institution;
    }

    public URI getSubjectsNotInCareer() {
        return subjectsNotInCareer;
    }

    public void setSubjectsNotInCareer(URI subjectsNotInCareer) {
        this.subjectsNotInCareer = subjectsNotInCareer;
    }

    public URI getSubjectCareers() {
        return subjectCareers;
    }

    public void setSubjectCareers(URI subjectCareers) {
        this.subjectCareers = subjectCareers;
    }
}
