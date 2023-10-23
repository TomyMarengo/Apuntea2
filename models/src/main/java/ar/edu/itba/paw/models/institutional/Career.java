package ar.edu.itba.paw.models.institutional;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "careers")
public class Career {
    @Id
    @Column(name = "career_id")
    private UUID careerId;
    @Column(name = "career_name")
    private String name;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Institution institution;

    // TODO: Remove?
    @Column(name = "institution_id")
    private UUID institutionId;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subjects_careers",
            joinColumns = @JoinColumn(name = "career_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;


    /* package-private */ Career() {

    }

    public Career(UUID careerId, String name) {
        this.careerId = careerId;
        this.name = name;
    }

    public Career(UUID careerId, String name, UUID institutionId) {
        this.careerId = careerId;
        this.name = name;
        this.institutionId = institutionId;
    }

    public UUID getCareerId() {
        return careerId;
    }

    public String getName() {
        return name;
    }

    public UUID getInstitutionId() {
        return institutionId;
    }

    @Override
    public int hashCode() {
        return careerId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Career)) return false;
        Career c = (Career) o;
        return c.careerId.equals(careerId);
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }
}
