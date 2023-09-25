package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Institution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstitutionDao {
    List<Institution> getInstitutions();

    Optional<Institution> findInstitutionById(UUID institutionId);
}
