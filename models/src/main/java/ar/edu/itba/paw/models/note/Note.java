package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class Note implements Searchable {
    private final UUID noteId;
    private final String name;
    private final User user;
    private final UUID parentId;
    private final Subject subject;
    private final Category category;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;
    private final String fileType;
    private final Float avgScore;
    private final Boolean visible;

    private Note(NoteBuilder builder) {
        this.noteId = builder.noteId;
        this.name = builder.name;
        this.user = builder.user;
        this.parentId = builder.parentId;
        this.subject = builder.subject;
        this.category = builder.category;
        this.createdAt = builder.createdAt;
        this.lastModifiedAt = builder.lastModifiedAt;
        this.visible = builder.visible;
        this.fileType = builder.fileType;
        this.avgScore = builder.avgScore;
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

    public static class NoteBuilder {
        private UUID noteId;
        private String name;
        private User user;
        private UUID parentId;
        private Subject subject;
        private Category category;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private Boolean visible;
        private String fileType;
        private Float avgScore;

        public NoteBuilder noteId(UUID noteId) {
            this.noteId = noteId;
            return this;
        }

        public NoteBuilder name(String name) {
            this.name = name;
            return this;
        }

        public NoteBuilder user(User user) {
            this.user = user;
            return this;
        }

        public NoteBuilder parentId(UUID parentId) {
            this.parentId = parentId;
            return this;
        }

        public NoteBuilder subject(Subject subject) {
            this.subject = subject;
            return this;
        }

        public NoteBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public NoteBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NoteBuilder lastModifiedAt(LocalDateTime lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
            return this;
        }

        public NoteBuilder visible(Boolean visible) {
            this.visible = visible;
            return this;
        }

        public NoteBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public NoteBuilder avgScore(Float avgScore) {
            this.avgScore = avgScore;
            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }

}
