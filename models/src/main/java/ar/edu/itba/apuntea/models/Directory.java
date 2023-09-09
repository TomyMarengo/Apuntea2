package ar.edu.itba.apuntea.models;

import java.util.UUID;

public class Directory {
    private UUID directoryId;
    private String name;
    private UUID parentId;
    private UUID userId;


    public Directory(UUID directoryId, String name, UUID parentId) {
        this.directoryId = directoryId;
        this.name = name;
        this.parentId = parentId;
    }

    public Directory(UUID directoryId, String name, UUID parentId, UUID userId) {
        this.directoryId = directoryId;
        this.name = name;
        this.parentId = parentId;
        this.userId = userId;
    }

    public String getName() { return name; }

    public UUID getDirectoryId() { return directoryId; }

    public UUID getParentId() { return parentId; }
}
