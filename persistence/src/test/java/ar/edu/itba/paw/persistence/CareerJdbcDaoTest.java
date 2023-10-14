package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

public class CareerJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private CareerJdbcDao careerDao;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert jdbcSubjectsCareersInsert;


    @Before
    public void setUp() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
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
    public void testCountCareersBySubjectId() {
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID career1Id = insertCareer(namedParameterJdbcTemplate, "career1", ITBA_ID);
        UUID career2Id = insertCareer(namedParameterJdbcTemplate, "career2", ITBA_ID);
        UUID career3Id = insertCareer(namedParameterJdbcTemplate, "career3", ITBA_ID);
        UUID career4Id = insertCareer(namedParameterJdbcTemplate, "career4", ITBA_ID);
        UUID career5Id = insertCareer(namedParameterJdbcTemplate, "career5", ITBA_ID);

        UUID subject1Id = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
        UUID subject2Id = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
        UUID subject3Id = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);

        insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, career1Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, career3Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, career5Id, 1);

        insertSubjectCareer(jdbcSubjectsCareersInsert, subject2Id, career2Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, subject2Id, career3Id, 1);

        int qtySubject1 = careerDao.countCareersBySubjectId(subject1Id);
        int qtySubject2 = careerDao.countCareersBySubjectId(subject2Id);
        int qtySubject3 = careerDao.countCareersBySubjectId(subject3Id);

        assertEquals(3, qtySubject1);
        assertEquals(2, qtySubject2);
        assertEquals(0, qtySubject3);

    }
}
