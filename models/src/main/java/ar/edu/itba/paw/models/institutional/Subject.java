package ar.edu.itba.paw.models.institutional;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID subjectId;
    @Column(name="subject_name")
    private String name;

    @Column(name = "root_directory_id")
    private UUID rootDirectoryId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subjects_careers",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "career_id")
    )
    private Set<Career> careers;

    private static final int MIN_YEAR = 1;
    private static final int MAX_YEAR = 10;

    //TODO: filter, dto or subjectcareer?
    @Transient
    private Integer year;

    /* package-private */ Subject() {

    }

    public Subject(String name, UUID rootDirectoryId) {
        this.name = name;
        this.rootDirectoryId = rootDirectoryId;
    }

    //TODO: for tests, remove?
    public Subject(UUID subjectId, String name, UUID rootDirectoryId, int year) {
        this.subjectId = subjectId;
        this.name = name;
        this.rootDirectoryId = rootDirectoryId;
        this.year =year;
    }

    public Subject(UUID subjectId, String name, UUID rootDirectoryId) {
        this.subjectId = subjectId;
        this.name = name;
        this.rootDirectoryId = rootDirectoryId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }
    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < MIN_YEAR || year > MAX_YEAR)
            throw new IllegalArgumentException();
        this.year = year;
    }

    public UUID getRootDirectoryId() {
        return rootDirectoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return subjectId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Subject)) return false;
        Subject s = (Subject) o;
        return s.subjectId.equals(subjectId);
    }

    public Collection<Career> getCareers() {
        return careers;
    }
}
