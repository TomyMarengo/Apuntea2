package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.directory.Directory;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.net.URI;
import java.util.UUID;

public class DirectoryDto {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String iconColor;
    private Boolean visible;

    private UUID parentId;

    //URIS
    private URI self;
    private URI owner;
    private URI parent;
    private URI subject;
    private URI notes;
    private URI directories;

    public static DirectoryDto fromDirectory(Directory directory, UriInfo uriInfo){
        final DirectoryDto directoryDto = new DirectoryDto();
        directoryDto.id = directory.getId();
        directoryDto.name = directory.getName();
        directoryDto.createdAt = directory.getCreatedAt();
        directoryDto.lastModifiedAt = directory.getLastModifiedAt();
        directoryDto.iconColor = directory.getIconColor();
        directoryDto.visible = directory.isVisible();

        directoryDto.self = uriInfo.getBaseUriBuilder().path("directories").path(directory.getId().toString()).build();
        if (directory.getUser() != null)
            directoryDto.owner = uriInfo.getBaseUriBuilder().path("users").path(directory.getUser().getUserId().toString()).build();
        if(directory.getParentId() != null){
            directoryDto.parent = uriInfo.getBaseUriBuilder().path("directories").path(directory.getParentId().toString()).build();
            directoryDto.parentId = directory.getParentId();
        }
        if (directory.getSubject() != null){
            directoryDto.subject = uriInfo.getBaseUriBuilder().path("subjects").path(directory.getSubject().getSubjectId().toString()).build();
        }
        directoryDto.notes = uriInfo.getBaseUriBuilder().path("notes").queryParam("parentId", directory.getId().toString()).build();
        directoryDto.directories = uriInfo.getBaseUriBuilder().path("directories").queryParam("parentId", directory.getId().toString()).build();
        return directoryDto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getOwner() {
        return owner;
    }

    public void setOwner(URI owner) {
        this.owner = owner;
    }

    public URI getParent() {
        return parent;
    }

    public void setParent(URI parent) {
        this.parent = parent;
    }

    public URI getSubject() {
        return subject;
    }

    public void setSubject(URI subject) {
        this.subject = subject;
    }

    public URI getNotes() {
        return notes;
    }

    public void setNotes(URI notes) {
        this.notes = notes;
    }

    public URI getDirectories() {
        return directories;
    }

    public void setDirectories(URI directories) {
        this.directories = directories;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
