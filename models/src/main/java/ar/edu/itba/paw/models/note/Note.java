package ar.edu.itba.paw.models.note;import ar.edu.itba.paw.models.Category;import ar.edu.itba.paw.models.institutional.Subject;import ar.edu.itba.paw.models.search.Searchable;import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;import ar.edu.itba.paw.models.user.User;import org.hibernate.annotations.Formula;import javax.persistence.*;import java.time.LocalDateTime;import java.util.Objects;import java.util.UUID;@Entity@Table(name = "notes")public class Note implements Searchable {    @Id    @Column(name = "note_id")    @GeneratedValue(strategy = GenerationType.AUTO)    private UUID noteId;    @Column(name = "note_name", nullable = false)    private String name;    @ManyToOne(fetch = FetchType.LAZY, optional = false)    @JoinColumn(name = "user_id", nullable = false)    private User user;    @Column(name = "parent_id", nullable = false)    private UUID parentId;    @ManyToOne(fetch = FetchType.LAZY, optional = false)    @JoinColumn(name = "subject_id", nullable = false)    private Subject subject;    @Column(nullable = false)    @Enumerated(EnumType.STRING)    private Category category;    @Column(name = "created_at", nullable = false)    @Convert(converter = LocalDateTimeConverter.class)    private LocalDateTime createdAt;    @Column(name = "last_modified_at", nullable = false)    @Convert(converter = LocalDateTimeConverter.class)    private LocalDateTime lastModifiedAt;    @Formula("(SELECT COUNT(*) FROM user_note_interactions uni WHERE uni.note_id = note_id)")    private long interactions = 0;    @Column(name = "file_type", nullable = false)    private String fileType;    @Formula("(SELECT COALESCE(AVG(r.score), 0) FROM reviews r WHERE r.note_id = note_id)")    private float avgScore = 0.0f;    @Column(nullable = false)    private boolean visible = true;    @Transient    private boolean favorite = false;    /* package-private */ Note() {}    @Override    public boolean equals(Object o) {        if (this == o) return true;        if (o == null || getClass() != o.getClass()) return false;        Note note = (Note) o;        return Objects.equals(noteId, note.noteId);    }    @Override    public int hashCode() {        return Objects.hash(noteId);    }    private Note(NoteBuilder builder) {        this.noteId = builder.noteId;        this.name = builder.name;        this.user = builder.user;        this.parentId = builder.parentId;        this.subject = builder.subject;        this.category = builder.category;        this.createdAt = builder.createdAt;        this.lastModifiedAt = builder.lastModifiedAt;        this.visible = builder.visible;        this.fileType = builder.fileType;    }    @PrePersist    protected void onCreate() {        lastModifiedAt = LocalDateTime.now();        createdAt = LocalDateTime.now();    }    @PreUpdate    protected void onUpdate() {        lastModifiedAt = LocalDateTime.now();    }    @Override    public UUID getId() {        return noteId;    }    @Override    public UUID getParentId() {        return parentId;    }    @Override    public User getUser() {        return user;    }    @Override    public Subject getSubject() {        return subject;    }    @Override    public String getName() {        return name;    }    @Override    public LocalDateTime getCreatedAt() {        return createdAt;    }    @Override    public LocalDateTime getLastModifiedAt() {        return lastModifiedAt;    }    @Override    public boolean isVisible() {        return visible;    }    @Override    public Category getCategory() {        return category;    }    @Override    public String getFileType() {        return fileType;    }    @Override    public float getAvgScore(){        return avgScore;    }    @Override    public String getIconColor() {        throw new UnsupportedOperationException();    }    @Override    public boolean isFavorite() {        return favorite;    }    public void setName(String name) {        this.name = name;    }    public void setCategory(Category category) {        this.category = category;    }    public void setVisible(boolean visible) {        this.visible = visible;    }    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {        this.lastModifiedAt = lastModifiedAt;    }    public void setFavorite(boolean favorite) {        this.favorite = favorite;    }    public long getInteractions() {        return interactions;    }    public static class NoteBuilder {        private UUID noteId;        private String name;        private User user;        private UUID parentId;        private Subject subject;        private Category category;        private LocalDateTime createdAt;        private LocalDateTime lastModifiedAt;        private boolean visible = true;        private String fileType;        public NoteBuilder id(UUID noteId) {            this.noteId = noteId;            return this;        }        public NoteBuilder name(String name) {            this.name = name;            return this;        }        public NoteBuilder user(User user) {            this.user = user;            return this;        }        public NoteBuilder parentId(UUID parentId) {            this.parentId = parentId;            return this;        }        public NoteBuilder subject(Subject subject) {            this.subject = subject;            return this;        }        public NoteBuilder category(Category category) {            this.category = category;            return this;        }        public NoteBuilder createdAt(LocalDateTime createdAt) {            this.createdAt = createdAt;            return this;        }        public NoteBuilder lastModifiedAt(LocalDateTime lastModifiedAt) {            this.lastModifiedAt = lastModifiedAt;            return this;        }        public NoteBuilder visible(boolean visible) {            this.visible = visible;            return this;        }        public NoteBuilder fileType(String fileType) {            this.fileType = fileType;            return this;        }        public Note build() {            return new Note(this);        }    }}