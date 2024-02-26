package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Institution;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


@Repository
public class InstitutionJpaDao implements InstitutionDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Institution> getInstitutions() {
        return em.createQuery("SELECT i FROM Institution i ORDER BY i.name asc ", Institution.class).getResultList();
    }


    @Override
    public Optional<Institution> getInstitution(final UUID institutionId) {
        return em.createQuery("FROM Institution i WHERE i.id = :institutionId", Institution.class)
                .setParameter("institutionId", institutionId)
                .getResultList()
                .stream()
                .findFirst();
    }

}
