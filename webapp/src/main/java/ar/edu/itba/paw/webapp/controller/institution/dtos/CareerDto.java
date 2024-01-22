package ar.edu.itba.paw.webapp.controller.institution.dtos;

import ar.edu.itba.paw.models.institutional.Career;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;


public class CareerDto {
    private UUID id;
    private String name;
    private URI self;
    private URI institution;
    private URI subjects;


    public static CareerDto fromCareer(final Career career, final UriInfo uriInfo){
        final CareerDto careerDto = new CareerDto();
        careerDto.id = career.getCareerId();
        careerDto.name = career.getName();
        UriBuilder builder = uriInfo.getBaseUriBuilder();

        careerDto.institution = builder.path("institution").path(career.getInstitutionId().toString()).build();
        careerDto.self = builder.path("careers").path(career.getCareerId().toString()).build();
        careerDto.subjects = builder.path("subjects").build();
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
}
