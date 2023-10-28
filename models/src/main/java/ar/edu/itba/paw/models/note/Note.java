package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notes")
public class Note implements Searchable {

    @Id
    @Column(name = "note_id")
    private UUID noteId;

    @Column(name = "note_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Directory parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "created_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastModifiedAt;

    @Column(name = "file_type")
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
        this.parent = builder.parent;
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
    public Directory getParent() {
        return parent;
    }
    @Override
    public User getUser() {
        return user;
    }

    //@Override //not override
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

    public NoteFile getNoteFile() {
        return noteFile;
    }

    public static class NoteBuilder {
        private UUID noteId;
        private String name;
        private User user;
        private Directory parent;
        private Subject subject;
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

        public NoteBuilder parent(Directory parent) {
            this.parent = parent;
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
