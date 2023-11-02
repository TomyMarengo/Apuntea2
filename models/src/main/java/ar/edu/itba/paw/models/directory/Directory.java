package ar.edu.itba.paw.models.directory;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.search.Searchable;
import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "directories")
public class Directory implements Searchable {
    @Id
    @Column(name = "directory_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID directoryId;

    @Column(nullable = false, name="directory_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "parent_id")
    private UUID parentId;

//    private Subject subject;

    @Column(name = "created_at")
    @Convert(converter = LocalDateTimeConverter.class)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at")
    @Convert(converter = LocalDateTimeConverter.class)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private LocalDateTime lastModifiedAt;

    @Column(name = "icon_color")
    private String iconColor;

    private Boolean visible;

    @Transient
    private Boolean favorite = false;

    /* package-private */ Directory() {}

    private Directory(DirectoryBuilder builder) {
        this.directoryId = builder.directoryId;
        this.name = builder.name;
        this.user = builder.user;
        this.parentId = builder.parentId;
        this.createdAt = builder.createdAt;
        this.lastModifiedAt = builder.lastModifiedAt;
        this.visible = builder.visible;
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
//  @Override
//    public Subject getSubject() {
//        return subject;
//    }
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

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public static class DirectoryBuilder {
        private UUID directoryId;
        private String name;
        private User user;
        private UUID parentId;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private String iconColor = "000000";
        private Boolean visible = true;

        public DirectoryBuilder id(UUID directoryId) {
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

        public DirectoryBuilder iconColor(String iconColor) {
            this.iconColor = iconColor;
            return this;
        }

        public Directory build() {
            return new Directory(this);
        }
    }
}
