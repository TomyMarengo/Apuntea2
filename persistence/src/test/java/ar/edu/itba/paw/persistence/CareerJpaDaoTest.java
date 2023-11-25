package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.persistence.TestUtils.*;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class CareerJpaDaoTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CareerJpaDao careerDao;

    @Before
    public void setUp() {}

    @Test
    public void testGetCareerById() {
        UUID careerId = insertCareer(em, "career1", ITBA_ID).getCareerId();

        Optional<Career> maybeCareer = careerDao.getCareerById(careerId);

        assertTrue(maybeCareer.isPresent());
        assertEquals(careerId, maybeCareer.get().getCareerId());
        assertEquals("career1", maybeCareer.get().getName());
    }

    @Test
    public void testGetCareerByIdNotExisting() {
        UUID careerId = UUID.randomUUID();

        Optional<Career> maybeCareer = careerDao.getCareerById(careerId);

        assertFalse(maybeCareer.isPresent());
    }

    private class TestCountCareersBySubjectInserts {
        private final UUID subject1Id;
        private final UUID subject3Id;
        private TestCountCareersBySubjectInserts() {
            Directory dir = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1").user(null).parent(null));

            UUID s1Career1Id = insertCareer(em, "s1career1", ITBA_ID).getCareerId();
            UUID s1Career2Id = insertCareer(em, "s1career2", ITBA_ID).getCareerId();
            UUID s12Career1Id = insertCareer(em, "s12career1", ITBA_ID).getCareerId();
            UUID s12Career2Id = insertCareer(em, "s12career2", ITBA_ID).getCareerId();
            UUID s2Career1Id = insertCareer(em, "s2career1", ITBA_ID).getCareerId();

            subject1Id = insertSubject(em, "subject1", dir.getId()).getSubjectId();
            UUID subject2Id = insertSubject(em, "subject2", dir.getId()).getSubjectId();
            subject3Id = insertSubject(em, "subject3", dir.getId()).getSubjectId();

            insertSubjectCareer(em, subject1Id, s1Career1Id, 1);
            insertSubjectCareer(em, subject1Id, s1Career2Id, 1);
            insertSubjectCareer(em, subject1Id, s12Career1Id, 1);
            insertSubjectCareer(em, subject1Id, s12Career2Id, 1);

            insertSubjectCareer(em, subject2Id, s2Career1Id, 1);
            insertSubjectCareer(em, subject2Id, s12Career1Id, 1);
            insertSubjectCareer(em, subject2Id, s12Career2Id, 1);
        }
    }

    @Test
    public void testCountCareersBySubjectId() {
        TestCountCareersBySubjectInserts test = new TestCountCareersBySubjectInserts();
        int qtyBySubject1 = careerDao.countCareersBySubjectId(test.subject1Id);

        assertEquals(4, qtyBySubject1);
    }

    @Test
    public void testCountCareersBySubjectIdEmpty() {
        TestCountCareersBySubjectInserts test = new TestCountCareersBySubjectInserts();
        int qtyBySubject3 = careerDao.countCareersBySubjectId(test.subject3Id);

        assertEquals(0, qtyBySubject3);
    }
}
