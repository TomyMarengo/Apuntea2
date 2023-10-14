package ar.edu.itba.paw.models.directory;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class Directory implements Searchable {
    private final UUID directoryId;
    private final String name;
    private final User user;
    private final UUID parentId;
    private final Subject subject;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    private final String iconColor;

    private final Boolean visible;
    private final Boolean favorite;

    private Directory(DirectoryBuilder builder) {
        this.directoryId = builder.directoryId;
        this.name = builder.name;
        this.user = builder.user;
        this.parentId = builder.parentId;
        this.subject = builder.subject;
        this.createdAt = builder.createdAt;
        this.lastModifiedAt = builder.lastModifiedAt;
        this.visible = builder.visible;
        this.favorite = builder.favorite;
        this.iconColor = builder.iconColor;
    }

    public UUID getId() { return directoryId; }
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
        return Category.DIRECTORY;
    }
    @Override
    public String getFileType() {
        throw new UnsupportedOperationException();
    }
    @Override
    public float getAvgScore(){
        return 0;
    }
    @Override
    public String getIconColor() {
        return iconColor;
    }
    @Override
    public boolean getFavorite() {
        return favorite;
    }

    public static class DirectoryBuilder {
        private UUID directoryId;
        private String name;
        private User user;
        private UUID parentId;
        private Subject subject;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private String iconColor;
        private Boolean visible;
        private Boolean favorite;

        public DirectoryBuilder directoryId(UUID directoryId) {
            this.directoryId = directoryId;
            return this;
        }

        public DirectoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DirectoryBuilder user(User user) {
            this.user = user;
            return this;
        }

        public DirectoryBuilder parentId(UUID parentId) {
            this.parentId = parentId;
            return this;
        }

        public DirectoryBuilder subject(Subject subject) {
            this.subject = subject;
            return this;
        }

        public DirectoryBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DirectoryBuilder lastModifiedAt(LocalDateTime lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
            return this;
        }

        public DirectoryBuilder visible(Boolean visible) {
            this.visible = visible;
            return this;
        }

        public DirectoryBuilder favorite(Boolean favorite) {
            this.favorite = favorite;
            return this;
        }

        public DirectoryBuilder iconColor(String iconColor) {
            this.iconColor = iconColor;
            return this;
        }

        public Directory build() {
            return new Directory(this);
        }
    }
}
