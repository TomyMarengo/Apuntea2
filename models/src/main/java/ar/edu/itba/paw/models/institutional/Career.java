package ar.edu.itba.paw.models.institutional;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "careers")
public class Career implements Serializable {
    @Id
    @Column(name = "career_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    private UUID careerId;
    @Column(name = "career_name")
    @Expose
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Institution institution;

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

    public UUID getCareerId() {
        return careerId;
    }

    public String getName() {
        return name;
    }

    public UUID getInstitutionId() {
        return institution.getInstitutionId();
    }

    public Institution getInstitution() {
        return institution;
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
