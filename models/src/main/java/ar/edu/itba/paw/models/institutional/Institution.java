package ar.edu.itba.paw.models.institutional;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "institutions")
public class Institution {
    @Id
    @Column(name = "institution_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID institutionId;
    @Column(name = "institution_name")
    private String name;

    @OneToMany(mappedBy = "institutionId", fetch = FetchType.LAZY)
    private Set<Career> careers;

    /* package-private */ Institution(){

    }

    // TODO: Remove
    public Institution(UUID institutionId, String name) {
        this.institutionId = institutionId;
        this.name = name;
    }
    public UUID getInstitutionId() {
        return institutionId;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return institutionId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Institution)) return false;
        Institution i = (Institution) o;
        return i.institutionId.equals(institutionId);
    }

    public Set<Career> getCareers() {
        return careers;
    }
}
