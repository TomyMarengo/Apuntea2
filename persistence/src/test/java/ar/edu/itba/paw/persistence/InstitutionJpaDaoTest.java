package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.InstitutionData;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.HashSet;
import java.util.Set;

import static ar.edu.itba.paw.persistence.TestUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class InstitutionJpaDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private InstitutionJpaDao institutionDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGetDefaultInstitutions() {
        InstitutionData data = institutionDao.getInstitutionData();
        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, INSTITUTIONS), data.getInstitutions().size());
    }

    @Test
    public void testGetDefaultCareers() {
        InstitutionData data = institutionDao.getInstitutionData();
        for (Institution institution : data.getInstitutions()) {
            assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, CAREERS, "institution_id = '" + institution.getInstitutionId() + "'"), data.getCareers(institution.getInstitutionId()).size());
        }
    }

    @Test
    public void testGetDefaultSubjects() {
        InstitutionData data = institutionDao.getInstitutionData();
        for (Institution institution : data.getInstitutions()) {
            for (Career career : data.getCareers(institution.getInstitutionId())) {
                assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "career_id = '" + career.getCareerId() + "'"), data.getSubjects(career.getCareerId()).size());
            }
        }
    }

    @Test
    public void testQuantitiesEqualTotalSubjects() {
        InstitutionData data = institutionDao.getInstitutionData();
        Set<Career> careers = new HashSet<>();
        Set<Subject> subjects = new HashSet<>();
        for (Institution institution : data.getInstitutions()) {
            careers.addAll(data.getCareers(institution.getInstitutionId()));
            for (Career career : data.getCareers(institution.getInstitutionId())) {
                subjects.addAll(data.getSubjects(career.getCareerId()));
            }
        }
        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, CAREERS), careers.size());
        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, SUBJECTS), subjects.size());
    }

    @Test
    public void testFindEDA() {
        InstitutionData data = institutionDao.getInstitutionData();
        Institution institution = data.getInstitutions().stream().filter(i -> i.getInstitutionId().equals(ITBA_ID)).findFirst().orElseThrow(AssertionError::new);
        assertEquals(ITBA_ID, institution.getInstitutionId());
        Career career = data.getCareers(ITBA_ID).stream().filter(c -> c.getCareerId().equals(ING_INF_ID)).findFirst().orElseThrow(AssertionError::new);
        assertEquals(ING_INF_ID, career.getCareerId());
        Subject subject = data.getSubjects(ING_INF_ID).stream().filter(s -> s.getSubjectId().equals(EDA_ID)).findFirst().orElseThrow(AssertionError::new);
        assertEquals(EDA_ID, subject.getSubjectId());
    }
}
