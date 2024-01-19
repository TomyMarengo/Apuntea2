package ar.edu.itba.paw.webapp.controller.subject.dtos;

import java.util.UUID;

public class SubjectCreationDto {
    private String name;
    private UUID rootDirectoryId;

    public SubjectCreationDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getRootDirectoryId() {
        return rootDirectoryId;
    }

    public void setRootDirectoryId(UUID rootDirectoryId) {
        this.rootDirectoryId = rootDirectoryId;
    }
}
