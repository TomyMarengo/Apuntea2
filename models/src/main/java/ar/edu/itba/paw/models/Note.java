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
    private Float avgScore;

    private Boolean visible;

    // For Reviews
    public Note(UUID noteId, String name, User user) {
        this.noteId = noteId;
        this.name = name;
        this.user = user;
    }

    // For update
    public Note(UUID noteId, String name, Category category, boolean visible) {
        this.noteId = noteId;
        this.name = name;
        this.category = category;
        this.visible = visible;
    }

    // For navigation
    public Note(UUID noteId, String name, User user, UUID parentId,  Category category, LocalDateTime createdAt, LocalDateTime lastModifiedAt, boolean visible, String file_type, float avgScore) {
        this.noteId = noteId;
        this.name = name;
        this.user = user;
        this.parentId = parentId;
        this.category = category;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.visible = visible;
        this.fileType = file_type;
        this.avgScore = avgScore;
    }

    // For Search
    public Note(UUID noteId, String name, User user, UUID parentId, Subject subject,  Category category, LocalDateTime createdAt, LocalDateTime lastModifiedAt, boolean visible, String file_type, float avgScore) {
        this(noteId, name, user, parentId, category, createdAt, lastModifiedAt, visible, file_type, avgScore);
        this.subject = subject;
    }

    // For admin delete
    public Note(UUID noteId, String name, User user, Category category, String file_type) {
        this.noteId = noteId;
        this.name = name;
        this.user = user;
        this.category = category;
        this.fileType = file_type;
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
    @Override
    public boolean isVisible() {
        return visible;
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
    public boolean getFavorite() {
        // TODO: Implement note favorites
        return false;
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
