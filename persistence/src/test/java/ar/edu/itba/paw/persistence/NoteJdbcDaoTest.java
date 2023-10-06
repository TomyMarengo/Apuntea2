package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static ar.edu.itba.paw.persistence.JdbcTestUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;

import java.util.HashMap;
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
    private SimpleJdbcInsert jdbcNoteInsert;
    private SimpleJdbcInsert jdbcDirectoryInsert;
    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcNoteInsert = new SimpleJdbcInsert(ds)
                .withTableName(NOTES)
                .usingColumns(NOTE_ID, NOTE_NAME, USER_ID, SUBJECT_ID, PARENT_ID, VISIBLE, CATEGORY, FILE_TYPE);
        jdbcDirectoryInsert = new SimpleJdbcInsert(ds)
                .withTableName(DIRECTORIES)
                .usingColumns(DIRECTORY_ID, DIRECTORY_NAME, USER_ID, PARENT_ID);
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

    // TODO: Create another test for note with parent
    @Test
    public void testCreateNote() {
        UUID noteId = noteDao.create("RBT", EDA_ID, PEPE_ID, true, new byte[]{1, 2, 3}, "practice", "jpg");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_name = 'RBT' AND user_id = '" + PEPE_ID + "' AND subject_id = '" + EDA_ID + "' AND category = 'practice'"));
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "'");
    }

    @Test
    public void testCreateNoteInDirectory() {
        UUID noteId = noteDao.create("RBT", EDA_ID, PEPE_ID, EDA_DIRECTORY_ID,true, new byte[]{1, 2, 3}, "practice", "jpg");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_name = 'RBT' AND user_id = '" + PEPE_ID + "' AND subject_id = '" + EDA_ID + "' AND category = 'practice'"));
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "'");
    }

    @Test
    public void testDeleteNote() {
        boolean deleted = noteDao.delete(PARCIAL_DINAMICA_FLUIDOS_NOTE_ID, PEPE_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
        assertTrue(deleted);
    }

    @Test
    public void testCannotDeleteNote() {
        boolean deleted = noteDao.delete(PARCIAL_DINAMICA_FLUIDOS_NOTE_ID, SAIDMAN_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
        assertFalse(deleted);
    }

    @Test
    public void testGetReviews() {
        List<Review> reviews = noteDao.getReviews(GUIA1EDA_NOTE_ID);
        assertEquals(2, reviews.size());
        assertEquals(PEPE_ID, reviews.get(0).getUser().getUserId());
    }

    @Test
    public void testDeleteMany() {
        UUID[] noteIds = {TMP_NOTE_ID_1, TMP_NOTE_ID_2, TMP_NOTE_ID_3, TMP_NOTE_ID_4};
        String[] names = {"tmp1", "tmp2", "tmp3", "tmp4"};
        jdbcDirectoryInsert.execute(new HashMap<String, Object>(){{
            put(DIRECTORY_ID, TMP_PARENT_DIR_ID);
            put(DIRECTORY_NAME, "tmp");
            put(USER_ID, PEPE_ID);
            put(PARENT_ID, EDA_DIRECTORY_ID);
        }});
        HashMap<String, Object> args = new HashMap<String, Object>(){{
            put(USER_ID, PEPE_ID);
            put(SUBJECT_ID, EDA_ID);
            put(PARENT_ID, TMP_PARENT_DIR_ID);
            put(VISIBLE, true);
            put(CATEGORY, "practice");
            put(FILE_TYPE, "pdf");
        }};
        for (int i = 0; i < noteIds.length; i++) {
            args.put(NOTE_NAME, names[i]);
            args.put(NOTE_ID, noteIds[i]);
            jdbcNoteInsert.execute(args);
        }
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + TMP_PARENT_DIR_ID + "'");
        noteDao.deleteMany(noteIds, PEPE_ID);
        int countPostDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + TMP_PARENT_DIR_ID + "'");
        assertEquals(4, countInserted);
        assertEquals(0, countPostDelete);
    }
}
