package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.InstitutionData;
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

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.YEAR;

//@Transactional
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//@Rollback
public class InstitutionJdbcDaoTest {
    //@Autowired
    private DataSource ds;
    //@Autowired
    private InstitutionJdbcDao institutionDao;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcSubjectsCareersInsert;
    //@Before
    public void setUp() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcSubjectsCareersInsert = new SimpleJdbcInsert(ds)
                .withTableName(SUBJECTS_CAREERS)
                .usingColumns(SUBJECT_ID, CAREER_ID, YEAR);
    }
}
