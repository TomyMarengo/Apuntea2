package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;

import java.util.ArrayList;
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
    private NoteJdbcDao noteDao;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }


    @Test
    public void testCreateReview(){
        noteDao.createOrUpdateReview(MVC_NOTE_ID, PEPE_ID , 5, "Muy buen apunte");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + MVC_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 5"));
    }

    @Test
    public void testUpdateReview(){
        int countScore4 = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4");
        noteDao.createOrUpdateReview(GUIA1EDA_NOTE_ID, PEPE_ID , 5, "Cacique in the JODA");
        assertEquals(1, countScore4);
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
        boolean deleted = noteDao.delete(new UUID[]{PARCIAL_DINAMICA_FLUIDOS_NOTE_ID}, PEPE_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
        assertTrue(deleted);
    }

    @Test
    public void testCannotDeleteNote() {
        boolean deleted = noteDao.delete(new UUID[]{PARCIAL_DINAMICA_FLUIDOS_NOTE_ID}, SAIDMAN_ID);
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
    public void testLimitReviews() {
        UUID[] userIds = new UUID[20];
        for (int i = 0; i < 20; i++)  {
            userIds[i] = insertStudent(namedParameterJdbcTemplate, "pepe" + i + "@itba.edu.ar", "", ING_INF, "es");
            insertReview(namedParameterJdbcTemplate, GUIA1EDA_NOTE_ID, userIds[i], (i % 5) + 1, "review " + i);
        }
        List<Review> reviews = noteDao.getReviews(GUIA1EDA_NOTE_ID);
        assertEquals(10, reviews.size());

        for (int i = 0; i < reviews.size() - 1; i++) {
            assertFalse(reviews.get(i).getCreatedAt().isBefore(reviews.get(i + 1).getCreatedAt()));
        }
    }

    @Test
    public void testDeleteMany() {
        List<UUID> noteIds = new ArrayList<>();
        String[] names = {"tmp1", "tmp2", "tmp3", "tmp4"};
        UUID[] ids = new UUID[names.length];
        UUID parentId = insertDirectory(namedParameterJdbcTemplate, "tmpParent", PEPE_ID, EDA_DIRECTORY_ID);
        for (String name : names) {
            UUID newDirId = insertNote(namedParameterJdbcTemplate, parentId, name, EDA_ID, PEPE_ID, true, new byte[]{1, 2, 3}, "practice", "jpg");
            noteIds.add(newDirId);
        }
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parentId + "'");
        noteDao.delete(noteIds.toArray(ids), PEPE_ID);
        int countPostDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parentId + "'");
        assertEquals(4, countInserted);
        assertEquals(0, countPostDelete);
    }
}
