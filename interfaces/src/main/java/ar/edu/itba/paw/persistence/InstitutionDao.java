package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Institution;

import java.util.List;

public interface InstitutionDao {
    List<Institution> getInstitutions();
}
