package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.persistence.InstitutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


@Service
public class InstitutionServiceImpl implements InstitutionService{
    private final InstitutionDao institutionDao;

    @Autowired
    public InstitutionServiceImpl(InstitutionDao institutionDao) { this.institutionDao = institutionDao; }

    @Transactional(readOnly = true)
    @Override
    public Collection<Institution> getInstitutions() {
        return institutionDao.getInstitutions();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Institution> getInstitution(final UUID institutionId) {
        return institutionDao.getInstitution(institutionId);
    }
}
