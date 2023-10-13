package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.InstitutionData;

import java.util.*;

public interface InstitutionService {
    List<Institution> getInstitutions();
    Optional<Institution> findInstitutionById(UUID institutionId);
    InstitutionData getInstitutionData();
}
