package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.InstitutionDataDto;
import ar.edu.itba.paw.persistence.InstitutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Service
public class InstitutionServiceImpl implements InstitutionService{
    private final InstitutionDao institutionDao;

    @Autowired
    public InstitutionServiceImpl(InstitutionDao institutionDao) { this.institutionDao = institutionDao; }

    @Transactional
    @Override
    public InstitutionDataDto getInstitutionData() {
        Collection<Institution> institutions = institutionDao.getInstitutions();
        InstitutionDataDto institutionDataDto = new InstitutionDataDto();
        institutions.forEach(institutionDataDto::addInstitution);
        return institutionDataDto;
    }


}
