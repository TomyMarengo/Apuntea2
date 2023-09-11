package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.SearchArguments;
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

import javax.sql.DataSource;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class NoteJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private NoteDao noteDao;
    private JdbcTemplate jdbcTemplate;

    private static UUID ITBA_ID = UUID.fromString("10000000-0000-0000-0000-000000000000");
    private static UUID ING_INF = UUID.fromString("c0000000-0000-0000-0000-000000000000");
    private static UUID EDA_ID = UUID.fromString("50000000-0000-0000-0000-000000000000");
    private static UUID EDA_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000000");
    private static UUID MVC_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000002");
    private static UUID GUIA1EDA_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000000");
    private static UUID PEPE_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testSearchByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, null, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(5, notes.size());
    }

    @Test
    public void testSearchByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, null, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(3, notes.size());
    }

    @Test
    public void testBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, null, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(2, notes.size());
    }
    @Test
    public void testByCategory(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, "practice", null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(1, notes.size());
    }

    @Test
    public void testOrderBy(){
        SearchArguments sa = new SearchArguments(null, null, null, null, null, null, "name", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        for (int i = 0; i < notes.size() - 2; i++) {
            assertTrue(notes.get(i).getName().toUpperCase().compareTo(notes.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testByScore() {
        SearchArguments sa = new SearchArguments(null, null, null, null, 3.0f, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertTrue( notes.size() >= 2); // TODO: Prevent race conditions so this can equal 2
        notes.forEach(note -> assertTrue(note.getAvgScore() >= 3.0f));
    }

    @Test
    public void testByPage() {
        SearchArguments sa = new SearchArguments(null, null, null, null, null, null, null, true, 1, 2);
        List<Note> notes = noteDao.search(sa);
        assertEquals(2, notes.size());
    }

    @Test
    public void testByDirectory() {
        List<Note> notes = noteDao.getNotesByParentDirectoryId(EDA_DIRECTORY_ID);
        assertEquals(2, notes.size());
    }

    @Test
    public void testByWord() {
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "guIA", null, true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(2, notes.size());
    }

    @Test
    public void testCreateReview(){
        noteDao.createOrUpdateReview(MVC_NOTE_ID, PEPE_ID , 5);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + MVC_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 5"));
    }

    @Test
    public void testUpdateReview(){
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4"));
        noteDao.createOrUpdateReview(GUIA1EDA_NOTE_ID, PEPE_ID , 5);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 5"));
    }
}
