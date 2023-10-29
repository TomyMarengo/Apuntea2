package ar.edu.itba.paw.models.institutional.dtos;

import ar.edu.itba.paw.models.institutional.Institution;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class InstitutionDto implements Serializable {
    private final UUID institutionId;
    private final String name;

    public InstitutionDto(Institution institution) {
        this.institutionId = institution.getInstitutionId();
        this.name = institution.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstitutionDto that = (InstitutionDto) o;
        return Objects.equals(institutionId, that.institutionId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(institutionId, name);
    }
}
