package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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

    @Override
    public List<Career> getCareersByUserInstitution(User user) {
        return em.createQuery("SELECT DISTINCT c FROM Career c WHERE c.institution = (SELECT u.career.institution FROM User u WHERE u = :user)", Career.class)
                .setParameter("user", user)
                .getResultList();

    }
}
