package ar.edu.itba.paw.models.institutional.dtos;

import ar.edu.itba.paw.models.institutional.Career;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CareerDto implements Serializable {
    private final UUID careerId;
    private final String name;

    public CareerDto(Career career) {
        this.careerId = career.getCareerId();
        this.name = career.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CareerDto careerDto = (CareerDto) o;
        return Objects.equals(careerId, careerDto.careerId) && Objects.equals(name, careerDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(careerId, name);
    }
}
