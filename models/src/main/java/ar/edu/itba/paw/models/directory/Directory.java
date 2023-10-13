package ar.edu.itba.paw.models.directory;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class Directory implements Searchable {
    private final UUID directoryId;
    private String name;
    private User user;
    private UUID parentId;
//  private Institution institution;
//  private Career career;
    private Subject subject;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private String iconColor;

    private Boolean visible;
    private Boolean favorite;

    public Directory(UUID directoryId, String name, UUID parentId) {
        this.directoryId = directoryId;
        this.name = name;
        this.parentId = parentId;
    }

    public Directory(UUID directoryId, String name, boolean visible, String iconColor) {
        this.directoryId = directoryId;
        this.name = name;
        this.visible = visible;
        this.iconColor = iconColor;
    }

    public Directory(UUID directoryId, String name, User user, String iconColor) {
        this.directoryId = directoryId;
        this.name = name;
        this.user = user;
        this.iconColor = iconColor;
    }

    public Directory(UUID directoryId, String name, User user, UUID parentId, LocalDateTime createdAt, LocalDateTime lastModifiedAt, boolean visible, String iconColor) {
        this.directoryId = directoryId;
        this.name = name;
        this.user = user;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.visible = visible;
        this.iconColor = iconColor;
    }

    public Directory(UUID directoryId, String name, User user, UUID parentId, boolean favorite, LocalDateTime createdAt, LocalDateTime lastModifiedAt, boolean visible, String iconColor) {
        this(directoryId, name, user, parentId, createdAt, lastModifiedAt, visible, iconColor);
        this.favorite = favorite;
    }


    public Directory(UUID directoryId, String name, User user, UUID parentId, boolean favorite, Subject subject, LocalDateTime createdAt, LocalDateTime lastModifiedAt, boolean visible, String iconColor) {
        this(directoryId, name, user, parentId, favorite, createdAt, lastModifiedAt, visible, iconColor);
        this.subject = subject;
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

    //TODO: change when visibility is implemented
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
}
