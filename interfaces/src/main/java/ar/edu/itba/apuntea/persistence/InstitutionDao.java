package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Institution;

import java.util.List;

public interface InstitutionDao {
    List<Institution> getInstitutions();
}
