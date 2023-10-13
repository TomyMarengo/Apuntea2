package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.InstitutionData;

import java.util.*;

public interface InstitutionDao {
    List<Institution> getInstitutions();

    Optional<Institution> findInstitutionById(UUID institutionId);

    InstitutionData getInstitutionData();
}
