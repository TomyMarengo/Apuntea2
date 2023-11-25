package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Institution;
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

import java.util.Collection;

import static ar.edu.itba.paw.models.NameConstants.*;
import static ar.edu.itba.paw.persistence.TestUtils.countRows;
import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class InstitutionJpaDaoTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private InstitutionJpaDao institutionDao;

    @Before
    public void setUp() {}

    @Test
    public void testGetDefaultInstitutions() {
        Collection<Institution> data = institutionDao.getInstitutions();
        assertEquals(countRows(em, INSTITUTIONS), data.size());
    }

}
