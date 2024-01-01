package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.institutional.Career;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;


public class CareerDto {
    private String name;
    private URI self;
    private URI institution;
    private URI subjects;


    public static CareerDto fromCareer(final Career career, final UriInfo uriInfo, final UUID institutionId){
        final CareerDto careerDto = new CareerDto();
        careerDto.name = career.getName();
        UriBuilder builder = uriInfo.getBaseUriBuilder();

        careerDto.institution = builder.path("institution").path(institutionId.toString()).build();
        careerDto.self = builder.path("careers").path(career.getCareerId().toString()).build();
        careerDto.subjects = builder.path("subjects").build();
        return careerDto;
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

    public URI getInstitution() {
        return institution;
    }

    public void setInstitution(URI institution) {
        this.institution = institution;
    }

    public URI getSubjects() {
        return subjects;
    }

    public void setSubjects(URI subjects) {
        this.subjects = subjects;
    }
}
