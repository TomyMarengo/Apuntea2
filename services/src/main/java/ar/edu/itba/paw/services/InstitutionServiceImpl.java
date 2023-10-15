package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.InstitutionData;
import ar.edu.itba.paw.persistence.InstitutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class InstitutionServiceImpl implements InstitutionService{
    private final InstitutionDao institutionDao;

    @Autowired
    public InstitutionServiceImpl(InstitutionDao institutionDao) { this.institutionDao = institutionDao; }

    @Transactional
    @Override
    public InstitutionData getInstitutionData() {
        return institutionDao.getInstitutionData();
    }


}
