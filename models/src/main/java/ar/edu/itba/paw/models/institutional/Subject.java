package ar.edu.itba.paw.models.institutional;

import ar.edu.itba.paw.models.directory.Directory;

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

    @JoinColumn(name = "root_directory_id", referencedColumnName = "directory_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Directory rootDirectory;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subjects_careers",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "career_id")
    )
    private Set<Career> careers;

    private static final int MIN_YEAR = 1;
    private static final int MAX_YEAR = 10;

    // loaded only when related with a career
    @Transient
    private Integer year;

    /* package-private */ Subject() {

    }

    public Subject(String name, Directory rootDirectory) {
        this.name = name;
        this.rootDirectory = rootDirectory;
    }

    public Subject(UUID subjectId, String name, Integer year, Directory rootDirectory) {
        this.subjectId = subjectId;
        this.name = name;
        this.year =year;
        this.rootDirectory = rootDirectory;
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
        return rootDirectory.getId();
    }

    public Directory getRootDirectory() {
        return rootDirectory;
    }

    public void setName(String name) {
        this.name = name;
        this.rootDirectory.setName(name);
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
