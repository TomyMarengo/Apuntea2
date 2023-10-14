package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.InstitutionData;
import ar.edu.itba.paw.persistence.InstitutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InstitutionServiceImpl implements InstitutionService{
    private final InstitutionDao institutionDao;

    @Autowired
    public InstitutionServiceImpl(InstitutionDao institutionDao) { this.institutionDao = institutionDao; }

    @Transactional
    @Override
    public List<Institution> getInstitutions() {
        return institutionDao.getInstitutions();
    }

    @Transactional
    @Override
    public Optional<Institution> findInstitutionById(UUID institutionId) {
        return institutionDao.findInstitutionById(institutionId);
    }

    @Transactional
    @Override
    public InstitutionData getInstitutionData() {
        return institutionDao.getInstitutionData();
    }


}
