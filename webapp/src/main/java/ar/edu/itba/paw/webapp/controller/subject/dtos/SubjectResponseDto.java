package ar.edu.itba.paw.webapp.controller.subject.dtos;

import ar.edu.itba.paw.models.institutional.Subject;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

public class SubjectResponseDto {
    private UUID id;
    private String name;

    private URI self;
    private URI rootDirectory;

    public static SubjectResponseDto fromSubject(final Subject subject, final UriInfo uriInfo){
        final SubjectResponseDto subjectDto = new SubjectResponseDto();
        subjectDto.id = subject.getSubjectId();
        subjectDto.name = subject.getName();

        subjectDto.self = uriInfo.getBaseUriBuilder().path("subjects").path(subject.getSubjectId().toString()).build();
        subjectDto.rootDirectory = uriInfo.getBaseUriBuilder().path("directories").path(subject.getRootDirectoryId().toString()).build();
        return subjectDto;
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
    public URI getSelf(){ return self; }

    public URI getRootDirectory() { return rootDirectory; }

    public void setName(String name){
        this.name = name;
    }

    public void setSelf(URI self){
        this.self = self;
    }

    public void setRootDirectory(URI rootDirectory) { this.rootDirectory = rootDirectory; }
}
