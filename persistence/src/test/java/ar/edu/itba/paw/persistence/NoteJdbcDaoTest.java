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
    public void testGetReviews() {
        List<Review> reviews = noteDao.getReviews(GUIA1EDA_NOTE_ID);
        assertEquals(2, reviews.size());
        assertEquals(PEPE_ID, reviews.get(0).getUser().getUserId());
    }
}
