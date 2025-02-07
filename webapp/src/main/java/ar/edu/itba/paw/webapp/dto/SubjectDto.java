package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.institutional.Subject;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

public class SubjectDto {
    private UUID id;
    private String name;
    private UUID rootDirectoryId;

    private URI self;
    private URI rootDirectory;

    public static SubjectDto fromSubject(final Subject subject, final UriInfo uriInfo){
        final SubjectDto subjectDto = new SubjectDto();
        subjectDto.id = subject.getSubjectId();
        subjectDto.name = subject.getName();
        subjectDto.rootDirectoryId = subject.getRootDirectoryId();

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

    public UUID getRootDirectoryId() {
        return rootDirectoryId;
    }

    public void setRootDirectoryId(UUID rootDirectoryId) {
        this.rootDirectoryId = rootDirectoryId;
    }
}
