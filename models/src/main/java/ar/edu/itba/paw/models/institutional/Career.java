package ar.edu.itba.paw.models.institutional;

import java.util.UUID;

public class Career {
    private final UUID careerId;
    private final String name;

    private UUID institutionId;

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

}
