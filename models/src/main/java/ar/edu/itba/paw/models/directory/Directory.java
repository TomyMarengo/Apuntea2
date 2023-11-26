package ar.edu.itba.paw.models.directory;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.search.Searchable;
import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Directory parent;

    @OneToOne(mappedBy = "rootDirectory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Subject subject;

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

    private boolean visible = true;

    @Transient
    private boolean favorite = false;

    @Transient
    private int qtyFiles = 0;

    /* package-private */ Directory() {}

    private Directory(DirectoryBuilder builder) {
        this.directoryId = builder.directoryId;
        this.name = builder.name;
        this.user = builder.user;
        this.parent = builder.parent;
        this.createdAt = builder.createdAt;
        this.lastModifiedAt = builder.lastModifiedAt;
        this.visible = builder.visible;
        this.iconColor = builder.iconColor;
    }

    @Override
    public UUID getId() { return directoryId; }

    public boolean isRootDirectory() {
        return parent == null;
    }

    @Override
    public UUID getParentId() {
        if (parent == null) return null;
        return parent.getId();
    }

    @Override
    public User getUser() {
        return user;
    }
    @Override
    public Subject getSubject() {
        return parent == null ? subject : parent.getSubject();
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
    public boolean isFavorite() {
        return favorite;
    }

    public int getQtyFiles() {
        return qtyFiles;
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

    public void setQtyFiles(int qtyFiles) {
        this.qtyFiles = qtyFiles;
    }

    @PrePersist
    protected void onCreate() {
        lastModifiedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        return Objects.equals(directoryId, directory.directoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directoryId);
    }

    public static class DirectoryBuilder {
        private UUID directoryId;
        private String name;
        private User user;
        private Directory parent;
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

        public DirectoryBuilder parent(Directory parent) {
            this.parent = parent;
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
