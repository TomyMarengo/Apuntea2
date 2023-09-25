package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Institution;
import ar.edu.itba.paw.persistence.InstitutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstitutionServiceImpl implements InstitutionService{
    private final InstitutionDao institutionDao;

    @Autowired
    public InstitutionServiceImpl(InstitutionDao institutionDao) { this.institutionDao = institutionDao; }

    @Override
    public List<Institution> getInstitutions() {
        return institutionDao.getInstitutions();
    }

    @Override
    public Optional<Institution> findInstitutionById(UUID institutionId) {
        return institutionDao.findInstitutionById(institutionId);
    }

}
