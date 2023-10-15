package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
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
import java.util.Optional;
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
    public void testGetNoteByIdPublic() {
        String name = "public";
        UUID noteId = insertNote(namedParameterJdbcTemplate, EDA_DIRECTORY_ID, name, EDA_ID, PEPE_ID, true, new byte[]{1, 2, 3}, "practice", "jpg");
        Note note = noteDao.getNoteById(noteId, SAIDMAN_ID).orElseThrow(AssertionError::new);

        assertEquals(name, note.getName());
        assertEquals(EDA_ID, note.getSubject().getSubjectId());
        assertEquals(PEPE_ID, note.getUser().getUserId());
        assertEquals(EDA_DIRECTORY_ID, note.getParentId());
        assertEquals("practice".toUpperCase(), note.getCategory().name());
        assertEquals("jpg", note.getFileType());
    }

    @Test
    public void testGetNoteByIdPrivate() {
        String name = "private";
        UUID noteId = insertNote(namedParameterJdbcTemplate, EDA_DIRECTORY_ID, name, EDA_ID, PEPE_ID, false, new byte[]{1, 2, 3}, "practice", "jpg");

        Optional<Note> maybeNote = noteDao.getNoteById(noteId, SAIDMAN_ID);

        assertFalse(maybeNote.isPresent());
    }

    @Test
    public void testGetNoteByIdPrivateOwner() {
        String name = "private";
        UUID noteId = insertNote(namedParameterJdbcTemplate, EDA_DIRECTORY_ID, name, EDA_ID, PEPE_ID, false, new byte[]{1, 2, 3}, "practice", "jpg");

        Note note = noteDao.getNoteById(noteId, PEPE_ID).orElseThrow(AssertionError::new);

        assertEquals(noteId, note.getId());
        assertEquals(name, note.getName());
    }

    @Test
    public void testGetNoteByIdPrivateAdmin() {
        String name = "private";
        UUID adminId = insertAdmin(namedParameterJdbcTemplate, "admin@mail.com" , "admin", ING_MEC, "es");
        UUID noteId = insertNote(namedParameterJdbcTemplate, EDA_DIRECTORY_ID, name, EDA_ID, PEPE_ID, false, new byte[]{1, 2, 3}, "practice", "jpg");

        Optional<Note> maybeNote = noteDao.getNoteById(noteId, adminId);

        assertFalse(maybeNote.isPresent());
    }

    @Test
    public void testGetNoteFileByIdPublic() {
        String name = "public";
        String extension = "jpg";
        String mimeType = "image/jpeg";
        byte[] content = new byte[]{1, 2, 3};
        UUID noteId = insertNote(namedParameterJdbcTemplate, EDA_DIRECTORY_ID, name, EDA_ID, PEPE_ID, true, content, "practice", extension);

        NoteFile file = noteDao.getNoteFileById(noteId, SAIDMAN_ID).orElseThrow(AssertionError::new);

        assertEquals(mimeType, file.getMimeType());
        assertArrayEquals(content.clone(), file.getContent());
    }

    @Test
    public void testGetNoteFileByIdPrivate() {
        UUID adminId = insertAdmin(namedParameterJdbcTemplate, "admin@mail.com" , "admin", ING_MEC, "es");
        String name = "private";
        UUID noteId = insertNote(namedParameterJdbcTemplate, EDA_DIRECTORY_ID, name, EDA_ID, PEPE_ID, false, new byte[]{1, 2, 3}, "practice", "jpg");

        Optional<NoteFile> maybeFile = noteDao.getNoteFileById(noteId, adminId);

        assertFalse(maybeFile.isPresent());
    }

    @Test
    public void testGetNoteFileByIdPrivateOwner() {
        String name = "public";
        String extension = "jpg";
        String mimeType = "image/jpeg";
        byte[] content = new byte[]{1, 2, 3};
        UUID noteId = insertNote(namedParameterJdbcTemplate, EDA_DIRECTORY_ID, name, EDA_ID, PEPE_ID, false, content, "practice", extension);

        NoteFile file = noteDao.getNoteFileById(noteId, PEPE_ID).orElseThrow(AssertionError::new);

        assertEquals(mimeType, file.getMimeType());
        assertArrayEquals(content.clone(), file.getContent());
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
    public void testUpdateNote() {
        String oldName = "oldName";
        String newName = "newName";
        String oldCategory = "practice";
        String newCategory = "theory";
        boolean oldVisible = true;
        boolean newVisible = false;
        UUID parentId = EDA_DIRECTORY_ID;
        UUID userId = PEPE_ID;

        UUID noteId = insertNote(namedParameterJdbcTemplate, parentId, oldName, EDA_ID, userId, oldVisible, new byte[]{1, 2, 3}, oldCategory, "jpg");

        Note note = new Note.NoteBuilder()
                .noteId(noteId)
                .name(newName)
                .visible(newVisible)
                .category(Category.valueOf(newCategory.toUpperCase()))
                .build();
        int oldCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
        int newCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");
        boolean success = noteDao.update(note, userId);

        int oldCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
        int newCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");
        assertTrue(success);
        assertEquals(1, oldCountPrev);
        assertEquals(0, newCountPrev);
        assertEquals(0, oldCountPost);
        assertEquals(1, newCountPost);
    }

    @Test
    public void testUpdateNoteNotOwner() {
        UUID adminId = insertAdmin(namedParameterJdbcTemplate, "admin@mail.com" , "admin", ING_MEC, "es");
        String oldName = "oldName";
        String newName = "newName";
        String oldCategory = "practice";
        String newCategory = "theory";
        boolean oldVisible = true;
        boolean newVisible = false;
        UUID parentId = EDA_DIRECTORY_ID;
        UUID userId = PEPE_ID;

        UUID noteId = insertNote(namedParameterJdbcTemplate, parentId, oldName, EDA_ID, userId, oldVisible, new byte[]{1, 2, 3}, oldCategory, "jpg");

        Note note = new Note.NoteBuilder()
                .noteId(noteId)
                .name(newName)
                .visible(newVisible)
                .category(Category.valueOf(newCategory.toUpperCase()))
                .build();
        int oldCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
        int newCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");

        boolean success = noteDao.update(note, adminId);

        int oldCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
        int newCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");
        assertFalse(success);
        assertEquals(1, oldCountPrev);
        assertEquals(0, newCountPrev);
        assertEquals(1, oldCountPost);
        assertEquals(0, newCountPost);
    }

    @Test
    public void testGetReview() {
        UUID newUserId = insertStudent(namedParameterJdbcTemplate, "new@mail.com", "new", ING_INF, "es");
        insertReview(namedParameterJdbcTemplate, GUIA1EDA_NOTE_ID, newUserId, 4, "ta ok");

        Review review = noteDao.getReview(GUIA1EDA_NOTE_ID, newUserId);

        assertEquals(newUserId, review.getUser().getUserId());
        assertEquals(4, review.getScore().intValue());
        assertEquals("ta ok", review.getContent());
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

    @Test
    public void testDeleteReview(){
        int countReview = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4");
        noteDao.deleteReview(GUIA1EDA_NOTE_ID, PEPE_ID);
        assertEquals(1, countReview);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4"));
    }

}
