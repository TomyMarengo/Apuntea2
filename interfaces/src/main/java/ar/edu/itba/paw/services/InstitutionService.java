package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Career;
import ar.edu.itba.paw.models.Institution;
import ar.edu.itba.paw.models.InstitutionData;
import ar.edu.itba.paw.models.Subject;

import java.util.*;

public interface InstitutionService {
    List<Institution> getInstitutions();
    Optional<Institution> findInstitutionById(UUID institutionId);
    InstitutionData getInstitutionData();
}
