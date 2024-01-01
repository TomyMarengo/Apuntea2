package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.institutional.Subject;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

public class SubjectDto {
    private String name;
    private int year;

    private URI institution;
    private URI career;
    private URI self;
    private URI rootDirectory;

    public static SubjectDto fromSubject(final Subject subject, final UriInfo uriInfo, final UUID careerId, final UUID institutionId){
        final SubjectDto subjectDto = new SubjectDto();
        subjectDto.name = subject.getName();

        UriBuilder builder = uriInfo.getBaseUriBuilder();
        subjectDto.institution = builder.path("institutions").path(institutionId.toString()).build();
        subjectDto.career = builder.path("careers").path(careerId.toString()).build();
        subjectDto.self = builder.path("subjects").path(subject.getSubjectId().toString()).build();
        // TODO: Make sure that the id was already loaded
        subjectDto.rootDirectory = uriInfo.getBaseUriBuilder().path("directories").path(subject.getRootDirectoryId().toString()).build();
        return subjectDto;
    }

    public String getName(){
        return name;
    }

    public int getYear() { return year; }

    public URI getInstitution() { return institution; }

    public URI getCareer() { return career; }

    public URI getSelf(){ return self; }

    public URI getRootDirectory() { return rootDirectory; }

    public void setName(String name){
        this.name = name;
    }

    public void setYear(int year) { this.year = year; }

    public void setInstitution(URI institution) { this.institution = institution; }

    public void setCareer(URI career) { this.career = career; }

    public void setSelf(URI self){
        this.self = self;
    }

    public void setRootDirectory(URI rootDirectory) { this.rootDirectory = rootDirectory; }
}
