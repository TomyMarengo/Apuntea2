package ar.edu.itba.paw.webapp.controller.note.dtos;

import ar.edu.itba.paw.models.note.Note;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public class NoteResponseDto {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Boolean visible;

    private String category;

    private Long interactions;

    private String fileType;

    private Float avgScore;

    //creation properties
    private UUID parentId;

    private UUID subjectId;

    //URIS
    private URI self;
    private URI owner;
    private URI parent;
    private URI subject;
    private URI file;
    private URI reviews;

    public NoteResponseDto() {
    }
    public static NoteResponseDto fromNote(Note note, UriInfo uriInfo){
        final NoteResponseDto noteDto = new NoteResponseDto();
        noteDto.id = note.getId();
        noteDto.name = note.getName();
        noteDto.createdAt = note.getCreatedAt();
        noteDto.lastModifiedAt = note.getLastModifiedAt();
        noteDto.visible = note.isVisible();
        noteDto.category = note.getCategory().name();
        noteDto.interactions = note.getInteractions();
        noteDto.fileType = note.getFileType();
        noteDto.avgScore = note.getAvgScore();

        noteDto.subject = uriInfo.getBaseUriBuilder().path("subjects").path(note.getSubject().getSubjectId().toString()).build();

        noteDto.self = uriInfo.getBaseUriBuilder().path("notes").path(note.getId().toString()).build();
        noteDto.file = uriInfo.getBaseUriBuilder().path("notes").path(note.getId().toString()).path("file").build();
        if (note.getUser() != null)
            noteDto.owner = uriInfo.getBaseUriBuilder().path("users").path(note.getUser().getUserId().toString()).build();
        if(note.getParentId() != null)
            noteDto.parent = uriInfo.getBaseUriBuilder().path("directories").path(note.getParentId().toString()).build();
        noteDto.reviews = uriInfo.getBaseUriBuilder().path("reviews").queryParam("noteId", note.getId().toString()).build();
        return noteDto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getInteractions() {
        return interactions;
    }

    public void setInteractions(Long interactions) {
        this.interactions = interactions;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Float getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Float avgScore) {
        this.avgScore = avgScore;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public URI getSubject() {
        return subject;
    }

    public void setSubject(URI subject) {
        this.subject = subject;
    }

    public URI getFile() {
        return file;
    }

    public void setFile(URI file) {
        this.file = file;
    }

    public URI getReviews() {
        return reviews;
    }

    public void setReviews(URI reviews) {
        this.reviews = reviews;
    }
}
