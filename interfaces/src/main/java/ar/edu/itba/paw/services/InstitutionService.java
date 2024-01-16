package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Institution;
//import ar.edu.itba.paw.models.institutional.dtos.InstitutionDataDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


public interface InstitutionService {
    Optional<Institution> getInstitution(final UUID institutionId);
    Collection<Institution> getInstitutions();
}
