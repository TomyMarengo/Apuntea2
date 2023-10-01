package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Institution;
import ar.edu.itba.paw.models.InstitutionData;

import java.util.*;

public interface InstitutionDao {
    List<Institution> getInstitutions();

    Optional<Institution> findInstitutionById(UUID institutionId);

    InstitutionData getInstitutionData();
}
