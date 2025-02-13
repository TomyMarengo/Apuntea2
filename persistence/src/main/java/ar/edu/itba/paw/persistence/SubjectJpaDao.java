package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.SubjectCareer;
import ar.edu.itba.paw.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    public Optional<SubjectCareer> getSubjectCareer(UUID subjectId, UUID careerId) {
        return em.createQuery("FROM SubjectCareer sc WHERE sc.subject.id = :subjectId AND sc.career.careerId = :careerId", SubjectCareer.class)
                .setParameter("subjectId", subjectId)
                .setParameter("careerId", careerId)
                .getResultList().stream().findFirst();
    }

    @Override
    public List<SubjectCareer> getSubjectCareers(UUID careerId) {
        return em.createQuery("FROM SubjectCareer sc WHERE sc.career.careerId = :careerId", SubjectCareer.class)
                .setParameter("careerId", careerId)
                .getResultList();
    }

    @Override
    public Optional<Subject> getSubjectById(UUID subjectId) {
        return Optional.ofNullable(em.find(Subject.class, subjectId));
    }

    @Override
    public List<Subject> getSubjects(UUID careerId, Integer year, User user) {
        if (careerId == null && user != null) { //by default, filter by the user's career
            careerId = user.getCareer().getCareerId();
        }

        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT DISTINCT s FROM Subject s, SubjectCareer sc WHERE sc.subject = s AND sc.career.careerId = :careerId ");
        queryCreator.addParameter("careerId", careerId);
        if (year != null) {
            queryCreator.append("AND sc.year = :year ");
            queryCreator.addParameter("year", year);
        }
        if (user != null) {
            queryCreator.append("AND (EXISTS(SELECT n FROM Note n WHERE n.parentId = s.rootDirectory.id AND n.user = :user) " +
                    "OR EXISTS(SELECT d FROM Directory d WHERE d.parent = s.rootDirectory AND d.user = :user)) ");
            queryCreator.addParameter("user", user);
        }

        queryCreator.append("ORDER BY s.name");

        TypedQuery<Subject> query = em.createQuery(queryCreator.createQuery(), Subject.class);
        queryCreator.getParams().forEach(query::setParameter);
        return query.getResultList();
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
    public Subject create(String name, UUID rootDirectoryId) {
        Subject newSubject = new Subject(name, em.getReference(Directory.class, rootDirectoryId));
        em.persist(newSubject);
        return newSubject;
    }

    @Override
    public void delete(Subject subject) {
        em.remove(subject);
    }

    @Override
    public boolean linkSubjectToCareer(Subject subject, UUID careerId, int year) {
        /* checks if the subject is linked with other institutions or with the same career */
        int count = ((BigInteger) em.createNativeQuery("SELECT COUNT(*) as n FROM Subjects_Careers sc " +
                        "INNER JOIN Subjects s ON sc.subject_id = s.subject_id " +
                        "INNER JOIN Careers c ON sc.career_id = c.career_id " +
                        "WHERE sc.subject_id = :subjectId " +
                        "AND ( c.institution_id != (SELECT c2.institution_id FROM Careers c2 WHERE c2.career_id = :careerId) OR sc.career_id = :careerId ) ")
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
        Optional<SubjectCareer> maybeSubjectCareer = em.createQuery("SELECT sc FROM SubjectCareer sc WHERE sc.subject.id = :subjectId AND sc.career.careerId = :careerId", SubjectCareer.class)
                .setParameter("subjectId", subjectId)
                .setParameter("careerId", careerId)
                .getResultList().stream().findFirst();
        if (maybeSubjectCareer.isPresent()) {
            maybeSubjectCareer.get().setYear(year);
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

