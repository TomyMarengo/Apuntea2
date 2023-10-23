package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class CareerJpaDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private CareerJpaDao careerDao;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcSubjectsCareersInsert;


    @Before
    public void setUp() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcSubjectsCareersInsert = new SimpleJdbcInsert(ds)
                .withTableName(SUBJECTS_CAREERS)
                .usingColumns(SUBJECT_ID, CAREER_ID, YEAR);
    }

    @Test
    public void testGetCareerById() {
        UUID careerId = insertCareer(namedParameterJdbcTemplate, "career1", ITBA_ID);

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
            UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);

            UUID s1Career1Id = insertCareer(namedParameterJdbcTemplate, "s1career1", ITBA_ID);
            UUID s1Career2Id = insertCareer(namedParameterJdbcTemplate, "s1career2", ITBA_ID);
            UUID s12Career1Id = insertCareer(namedParameterJdbcTemplate, "s12career1", ITBA_ID);
            UUID s12Career2Id = insertCareer(namedParameterJdbcTemplate, "s12career2", ITBA_ID);
            UUID s2Career1Id = insertCareer(namedParameterJdbcTemplate, "s2career1", ITBA_ID);

            subject1Id = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
            UUID subject2Id = insertSubject(namedParameterJdbcTemplate, "subject2", dirId);
            subject3Id = insertSubject(namedParameterJdbcTemplate, "subject3", dirId);

            insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, s1Career1Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, s1Career2Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, s12Career1Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, s12Career2Id, 1);

            insertSubjectCareer(jdbcSubjectsCareersInsert, subject2Id, s2Career1Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject2Id, s12Career1Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject2Id, s12Career2Id, 1);
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
