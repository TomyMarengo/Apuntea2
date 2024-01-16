package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Institution;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


public interface InstitutionDao {
    Collection<Institution> getInstitutions();
    Optional<Institution> getInstitution(UUID institutionId);
}
