package ar.edu.itba.paw.models.institutional;

import java.util.UUID;

public class Institution {
    private final UUID institutionId;
    private final String name;

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

}
