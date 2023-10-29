package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notes")
public class Note implements Searchable {

    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID noteId;

    @Column(name = "note_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "created_at", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastModifiedAt;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Formula("(SELECT COALESCE(AVG(r.score), 0) FROM reviews r WHERE r.note_id = note_id)")
    private Float avgScore;

    private Boolean visible;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private NoteFile noteFile;

    /* package-private */ Note() {}

    private Note(NoteBuilder builder) {
        this.noteId = builder.noteId;
        this.name = builder.name;
        this.user = builder.user;
        this.parentId = builder.parentId;
        this.subjectId = builder.subjectId;
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
    public UUID getParentId() {
        return parentId;
    }
    @Override
    public User getUser() {
        return user;
    }

    //@Override //not override
    public UUID getSubjectId() {
        return subjectId;
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

    public NoteFile getNoteFile() {
        return noteFile;
    }

    public void setNoteFile(NoteFile noteFile) {
        this.noteFile = noteFile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public static class NoteBuilder {
        private UUID noteId;
        private String name;
        private User user;
        private UUID parentId;
        private UUID subjectId;
        private Category category;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private Boolean visible;
        private String fileType;
        private Float avgScore;

        public NoteBuilder id(UUID noteId) {
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

        public NoteBuilder subjectId(UUID subjectId) {
            this.subjectId = subjectId;
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
