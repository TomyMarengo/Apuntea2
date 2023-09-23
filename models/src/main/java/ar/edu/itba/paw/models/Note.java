package ar.edu.itba.paw.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Note implements Searchable {
    private final UUID noteId;
    private String name;
    private User user;
    private UUID parentId;
//  private Institution institution;
//  private Career career;
    private Subject subject;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
//    private byte[] file;
    private String fileType;
    private Float avgScore; // Semantically, an empty avgScore

    // For Reviews
    public Note(UUID noteId, String name, User user) {
        this.noteId = noteId;
        this.name = name;
        this.user = user;
    }

    // For Search
    public Note(UUID noteId, String name, User user, UUID parentId, Subject subject,  Category category, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String file_type, float avgScore) {
        this.noteId = noteId;
        this.name = name;
        this.user = user;
        this.parentId = parentId;
        this.subject = subject;
        this.category = category;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.fileType = file_type;
        this.avgScore = avgScore;
    }

    @Override
    public UUID getId() {
        return noteId;
    }
    @Override
    public UUID getParentId() {
        return parentId;
    }
    @Override
    public User getUser() {
        return user;
    }
    @Override
    public Subject getSubject() {
        return subject;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    @Override
    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    //TODO: change when visibility is implemented
    @Override
    public boolean isVisible() {
        return true;
    }
    @Override
    public Category getCategory() {
        return category;
    }
    @Override
    public String getFileType() {
        return fileType;
    }
    @Override
    public float getAvgScore(){
        return avgScore;
    }
    @Override
    public String getIconColor() {
        throw new UnsupportedOperationException();
    }


    @Override
    public String toString() {
        return "Note{" +
                "name='" + name + '\'' +
                ", owner=" + user +
                '}';
    }
    
//    public byte[] getFile() { return file; }
}
