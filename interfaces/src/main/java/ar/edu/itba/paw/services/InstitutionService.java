package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Institution;
//import ar.edu.itba.paw.models.institutional.dtos.InstitutionDataDto;

import java.util.Collection;


public interface InstitutionService {
//    InstitutionDataDto getInstitutionData();
    Collection<Institution> getInstitutions();
}
