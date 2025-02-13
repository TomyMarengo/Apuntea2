package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.institutional.Institution;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

public class InstitutionDto {
    private UUID id;
    private String name;
    private URI self;
    private URI careers;

    public static InstitutionDto fromInstitution(final Institution institution, final UriInfo uriInfo){
        final InstitutionDto institutionDto = new InstitutionDto();
        institutionDto.id = institution.getInstitutionId();
        institutionDto.name = institution.getName();
        UriBuilder builder = uriInfo.getBaseUriBuilder();

        institutionDto.self = builder.path("institutions").path(institution.getInstitutionId().toString()).build();
        institutionDto.careers = builder.path("careers").build();
        return institutionDto;
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

    public URI getCareers(){
        return careers;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSelf(URI self){
        this.self = self;
    }

    public void setCareers(URI careers){
        this.careers = careers;
    }
}
