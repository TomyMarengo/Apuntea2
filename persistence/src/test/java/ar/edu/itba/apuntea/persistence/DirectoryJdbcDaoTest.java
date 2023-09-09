package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Category;
import ar.edu.itba.apuntea.models.Directory;
import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.models.SearchArguments;
import ar.edu.itba.apuntea.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DirectoryJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private DirectoryDao directoryDao;

    private JdbcTemplate jdbcTemplate;

    private static String ITBA_ID = "10000000-0000-0000-0000-000000000000";
    private static String ING_INF = "c0000000-0000-0000-0000-000000000000";
    private static String EDA_ID = "50000000-0000-0000-0000-000000000000";

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testSearchByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, null, null, "name", true);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(4, directories.size());
    }

    @Test
    public void testSearchByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, null, null, "name", true);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(2, directories.size());
    }

    @Test
    public void testBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, null, null, "name", true);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(1, directories.size());
    }

    @Test
    public void testOrderBy(){
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        for (int i = 0; i < directories.size() - 2; i++) {
            assertTrue(directories.get(i).getName().toUpperCase().compareTo(directories.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testByPage() {
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "name", true, 1, 2);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(2, directories.size());
    }
}
