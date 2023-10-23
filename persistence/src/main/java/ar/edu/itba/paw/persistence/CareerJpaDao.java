package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CareerJpaDao implements CareerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Career> getCareerById(UUID careerId) {
        return Optional.ofNullable(em.find(Career.class, careerId));
    }

    @Override
    public int countCareersBySubjectId(UUID subjectId) {
        Number qty = (Number) em.createNativeQuery("SELECT COUNT(*) FROM subjects_careers WHERE subject_id = :subjectId")
                .setParameter("subjectId", subjectId)
                .getSingleResult();
        return qty.intValue();
    }
}
