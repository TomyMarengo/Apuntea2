package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Institution;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.Collection;


@Repository
public class InstitutionJpaDao implements InstitutionDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Institution> getInstitutions() {
        final TypedQuery<Institution> query = em.createQuery("SELECT DISTINCT i FROM Institution i JOIN FETCH i.careers c JOIN FETCH c.subjects s", Institution.class);
        return query.getResultList();
    }
}
