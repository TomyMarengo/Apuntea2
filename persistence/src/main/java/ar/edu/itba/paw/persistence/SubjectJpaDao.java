package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.SubjectCareer;
import ar.edu.itba.paw.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SubjectJpaDao implements SubjectDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Subject> getSubjectById(UUID subjectId) {
        return Optional.ofNullable(em.find(Subject.class, subjectId));
    }

    @Override
    public List<Subject> getSubjectsByCareerId(UUID careerId) {
        List<SubjectCareer> subjectsCareers = em.createQuery("SELECT DISTINCT sc FROM SubjectCareer sc WHERE sc.career.careerId = :careerId ORDER BY sc.year", SubjectCareer.class)
                .setParameter("careerId", careerId)
                .getResultList();
        subjectsCareers.forEach(sc -> sc.getSubject().setYear(sc.getYear()));
        return subjectsCareers.stream().map(SubjectCareer::getSubject).collect(Collectors.toList());
    }

    @Override
    public UUID getSubjectByRootDirectoryId(UUID rootDirectoryId) {
        return em.createQuery("SELECT s.id FROM Subject s WHERE s.rootDirectory.id = :rootDirectoryId", UUID.class)
                .setParameter("rootDirectoryId", rootDirectoryId)
                .getSingleResult();
    }

    @Override
    public List<Subject> getSubjectsByCareerIdComplemented(UUID careerId) {
        return em.createQuery("SELECT DISTINCT sc1.subject FROM SubjectCareer sc1 " +
                        "WHERE sc1.subject NOT IN (SELECT sc2.subject FROM SubjectCareer sc2 WHERE sc2.career.careerId = :careerId) " +
                        "AND sc1.career.institution IN (SELECT sc3.career.institution FROM SubjectCareer sc3 WHERE sc3.career.careerId = :careerId)", Subject.class)
                .setParameter("careerId", careerId)
                .getResultList();
    }

    @Override
    public List<Subject> getSubjectsByUser(User user) {
        List<SubjectCareer> subjectsCareers = em.createQuery("SELECT DISTINCT sc FROM SubjectCareer sc JOIN sc.subject s JOIN s.rootDirectory rd " +
                        "WHERE sc.career.id = :careerId AND (EXISTS(SELECT n FROM Note n WHERE n.parentId = rd.id AND n.user.id = :userId) " +
                        "OR EXISTS(SELECT d FROM Directory d WHERE d.parent = rd AND d.user.id = :userId))", SubjectCareer.class)
                .setParameter("userId", user.getUserId())
                .setParameter("careerId", user.getCareer().getCareerId())
                .getResultList();
        subjectsCareers.forEach(sc -> sc.getSubject().setYear(sc.getYear()));
        return subjectsCareers.stream().map(SubjectCareer::getSubject).collect(Collectors.toList());
    }

    @Override
    public Subject create(String name, UUID rootDirectoryId) {
        Subject newSubject = new Subject(name, em.getReference(Directory.class, rootDirectoryId));
        em.persist(newSubject);
        return newSubject;
    }

    @Override
    public boolean delete(UUID subjectId) {
        return em.createQuery("DELETE FROM Subject s WHERE s.id = :subjectId")
                .setParameter("subjectId", subjectId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean linkSubjectToCareer(Subject subject, UUID careerId, int year) {
        int count = ((BigInteger) em.createNativeQuery("SELECT COUNT(*) as n FROM Subjects_Careers sc " +
                        "INNER JOIN Subjects s ON sc.subject_id = s.subject_id " +
                        "INNER JOIN Careers c ON sc.career_id = c.career_id " +
                        "WHERE sc.subject_id = :subjectId " +
                        "AND c.institution_id != (SELECT c2.institution_id FROM Careers c2 WHERE c2.career_id = :careerId)")
                .setParameter("careerId", careerId)
                .setParameter("subjectId", subject.getSubjectId())
                .getSingleResult()).intValue();
        if (count == 0) {
            SubjectCareer subjectCareer = new SubjectCareer(subject, em.getReference(Career.class, careerId), year);
            em.persist(subjectCareer);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSubjectCareer(UUID subjectId, UUID careerId, int year) {
        SubjectCareer subjectCareer = em.createQuery("SELECT sc FROM SubjectCareer sc WHERE sc.subject.id = :subjectId AND sc.career.careerId = :careerId", SubjectCareer.class)
                .setParameter("subjectId", subjectId)
                .setParameter("careerId", careerId)
                .getSingleResult();
        if (subjectCareer != null) {
            subjectCareer.setYear(year);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId) {
        return em.createQuery("DELETE FROM SubjectCareer sc WHERE sc.subject.id = :subjectId AND sc.career.careerId = :careerId")
                .setParameter("subjectId", subjectId)
                .setParameter("careerId", careerId)
                .executeUpdate() == 1;
    }
}

