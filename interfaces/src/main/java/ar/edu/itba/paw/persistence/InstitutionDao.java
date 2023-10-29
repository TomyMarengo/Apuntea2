package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Institution;

import java.util.Collection;


public interface InstitutionDao {
    Collection<Institution> getInstitutions();
}
