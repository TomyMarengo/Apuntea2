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
    private UUID subjectId;
    @Column(name="subject_name")
    private String name;

    // TODO: Implement directory JPA
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


    // TODO: Change!
    @Transient
    Integer year;

    /* package-private */ Subject() {

    }

    public Subject(UUID subjectId, String name) {
        this.subjectId = subjectId;
        this.name = name;
    }

    public Subject(UUID subjectId, String name, UUID rootDirectoryId) {
        this(subjectId, name);
        this.rootDirectoryId = rootDirectoryId;
    }

    //for subject-career relation
    public Subject(UUID subjectId, String name, int year) {
        this(subjectId, name);
        if (year > MAX_YEAR || year < MIN_YEAR)
            throw new IllegalArgumentException();
        this.year = year;
    }

    public Subject(UUID subjectId, String name, UUID rootDirectoryId, int year) {
        this(subjectId, name, year);
        this.rootDirectoryId = rootDirectoryId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }
    public String getName() {
        return name;
    }

    public UUID getRootDirectoryId() {
        return rootDirectoryId;
    }

    public Integer getYear() {
        return year;
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

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId:" + subjectId +
                ", name:'" + name + '\'' +
                '}';
    }

    public Collection<Career> getCareers() {
        return careers;
    }
}
