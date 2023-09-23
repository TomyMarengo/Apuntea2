package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Review;
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
import org.springframework.transaction.annotation.Transactional;

import static ar.edu.itba.paw.persistence.JdbcTestConstants.*;

import javax.sql.DataSource;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class NoteJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private NoteDao noteDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testSearchByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(6, notes.size());
    }

    @Test
    public void testSearchByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(4, notes.size());
    }

    @Test
    public void testBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(2, notes.size());
    }
    @Test
    public void testByCategory(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, "practice", null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(1, notes.size());
    }

    @Test
    public void testOrderBy(){
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "name", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        for (int i = 0; i < notes.size() - 2; i++) {
            assertTrue(notes.get(i).getName().toUpperCase().compareTo(notes.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testOrderByScore(){
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        for (int i = 0; i < notes.size() - 2; i++) {
            assertTrue(notes.get(i).getAvgScore() <= notes.get(i + 1).getAvgScore());
        }
    }

    // TODO: Uncomment when pagination is implemented
//    @Test
//    public void testByPage() {
//        SearchArguments sa = new SearchArguments(null, null, null, null, null, null, true, 1, 2);
//        List<Note> notes = noteDao.search(sa);
//        assertEquals(2, notes.size());
//    }

    @Test
    public void testByDirectory() {
        List<Note> notes = noteDao.getNotesByParentDirectoryId(EDA_DIRECTORY_ID);
        assertEquals(2, notes.size());
    }

    @Test
    public void testByWord() {
        SearchArguments sa = new SearchArguments(null, null, null, null, "guIA", null, true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(2, notes.size());
    }

    @Test
    public void testCreateReview(){
        noteDao.createOrUpdateReview(MVC_NOTE_ID, PEPE_ID , 5, "Muy buen apunte");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + MVC_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 5"));
    }

    @Test
    public void testUpdateReview(){
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4"));
        noteDao.createOrUpdateReview(GUIA1EDA_NOTE_ID, PEPE_ID , 5, "Cacique in the JODA");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 5"));
    }

    @Test
    public void testCreateNote() {
        UUID noteId = noteDao.create(new byte[]{1, 2, 3}, "RBT", PEPE_ID, EDA_ID, "practice", "jpg");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_name = 'RBT' AND user_id = '" + PEPE_ID + "' AND subject_id = '" + EDA_ID + "' AND category = 'practice'"));
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "'");
    }

    @Test
    public void testDeleteNote() {
        noteDao.delete(PARCIAL_DINAMICA_FLUIDOS_NOTE_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
    }

    @Test
    public void testMultipleCareerSubject() {
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, MATE_ID, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(1, notes.size());
        assertEquals(TVM_ID ,notes.get(0).getNoteId());
    }

    @Test
    public void testMultipleCareerSubjectBis() {
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_MEC , MATE_ID, null, null, "score", true, 1, 10);
        List<Note> notes = noteDao.search(sa);
        assertEquals(1, notes.size());
        assertEquals(TVM_ID, notes.get(0).getNoteId());
    }

    @Test
    public void testGetReviews() {
        List<Review> reviews = noteDao.getReviews(GUIA1EDA_NOTE_ID);
        assertEquals(2, reviews.size());
        assertEquals(PEPE_ID, reviews.get(0).getUser().getUserId());
    }
}
