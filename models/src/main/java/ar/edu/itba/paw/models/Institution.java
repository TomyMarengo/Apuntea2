package ar.edu.itba.paw.models;

import java.util.UUID;

public class Institution {
    private UUID institutionId;
    private String name;

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
}
