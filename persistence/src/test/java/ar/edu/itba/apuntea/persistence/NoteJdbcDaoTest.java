package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Category;
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
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)

public class NoteJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private NoteDao noteDao;
    private JdbcTemplate jdbcTemplate;

    private static String ITBA_ID = "10000000-0000-0000-0000-000000000000";
    private static String ING_INF = "c0000000-0000-0000-0000-000000000000";

    private static String EDA_ID = "50000000-0000-0000-0000-000000000000";

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void searchByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, null, null);
        List<Note> notes = noteDao.search(sa);
        assertEquals(notes.size(), 5);
    }

    @Test
    public void searchByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, null, null);
        List<Note> notes = noteDao.search(sa);
        assertEquals(notes.size(), 3);
    }

    @Test
    public void searchBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, null, null);
        List<Note> notes = noteDao.search(sa);
        assertEquals(notes.size(), 2);
    }
    @Test
    public void searchByCategory(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, Category.PRACTICE.toString(), null);
        List<Note> notes = noteDao.search(sa);
        assertEquals(notes.size(), 1);
    }
}
