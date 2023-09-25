package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Institution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstitutionService {
    List<Institution> getInstitutions();
    Optional<Institution> findInstitutionById(UUID institutionId);
}
