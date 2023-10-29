package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
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

import static ar.edu.itba.paw.persistence.TestUtils.*;
import static ar.edu.itba.paw.models.NameConstants.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.util.*;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class NoteJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private DataSource ds;
    @Autowired
    private NoteJpaDao noteDao;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private User pepeUser;
    private User carlaAdmin;
    private Note notePublic;
    private Note notePrivate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        pepeUser = em.find(User.class, PEPE_ID);
        carlaAdmin = em.find(User.class, CARLADMIN_ID);
        Note.NoteBuilder builder = new Note.NoteBuilder()
                .name("public")
                .subjectId(EDA_ID)
                .parentId(EDA_DIRECTORY_ID)
                .user(pepeUser)
                .visible(true)
                .category(Category.PRACTICE)
                .fileType("jpg");
        byte[] publicContent = new byte[]{1, 2, 3};
        notePublic = insertNote(em, builder, publicContent);
        builder.name("private").visible(false);
        byte[] privateContent = new byte[]{4, 5, 6};
        notePrivate = insertNote(em, builder, privateContent);
    }

    @Test
    public void testCreateNoteInDirectory() {
        UUID noteId = noteDao.create("RBT", EDA_ID, pepeUser, EDA_DIRECTORY_ID,true, new byte[]{1, 2, 3}, "PRACTICE", "jpg");
        em.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_name = 'RBT' AND user_id = '" + PEPE_ID + "' AND subject_id = '" + EDA_ID + "' AND category = 'PRACTICE'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "note_files", "note_id = '" + noteId + "'"));
    }

    @Test
    public void testGetNoteByIdPublic() {
        Note foundNote = noteDao.getNoteById(notePublic.getId(), SAIDMAN_ID).orElseThrow(AssertionError::new);

        assertEquals(notePublic, foundNote);
        assertEquals(notePublic.getName(), foundNote.getName());
        assertEquals(EDA_ID, foundNote.getSubjectId());
        assertEquals(PEPE_ID, foundNote.getUser().getUserId());
        assertEquals(EDA_DIRECTORY_ID, foundNote.getParentId());
        assertEquals("practice".toUpperCase(), foundNote.getCategory().name());
        assertEquals("jpg", foundNote.getFileType());
    }

    @Test
    public void testGetNoteByIdPrivate() {
        Optional<Note> maybeNote = noteDao.getNoteById(notePrivate.getId(), SAIDMAN_ID);

        assertFalse(maybeNote.isPresent());
    }

    @Test
    public void testGetNoteByIdPrivateOwner() {
        Note note = noteDao.getNoteById(notePrivate.getId(), PEPE_ID).orElseThrow(AssertionError::new);

        assertEquals(notePrivate.getId(), note.getId());
        assertEquals(notePrivate.getName(), note.getName());
    }

    @Test
    public void testGetNoteByIdPrivateAdmin() {
        User admin = insertAdmin(em, ADMIN_EMAIL , "admin", ING_MEC_ID, "es");

        Optional<Note> maybeNote = noteDao.getNoteById(notePrivate.getId(), admin.getUserId());

        assertFalse(maybeNote.isPresent());
    }

    @Test
    public void testGetNoteFileByIdPublic() {
        NoteFile file = noteDao.getNoteFileById(notePublic.getId(), SAIDMAN_ID).orElseThrow(AssertionError::new);

        assertEquals(notePublic.getNoteFile().getMimeType(), file.getMimeType());
        assertArrayEquals(notePublic.getNoteFile().getContent(), file.getContent());
    }

    @Test
    public void testGetNoteFileByIdPrivate() {
        Optional<NoteFile> maybeFile = noteDao.getNoteFileById(notePrivate.getId(), carlaAdmin.getUserId());

        assertFalse(maybeFile.isPresent());
    }

    @Test
    public void testGetNoteFileByIdPrivateOwner() {

        NoteFile file = noteDao.getNoteFileById(notePrivate.getId(), PEPE_ID).orElseThrow(AssertionError::new);

        assertEquals(notePrivate.getNoteFile().getMimeType(), file.getMimeType());
        assertArrayEquals(notePrivate.getNoteFile().getContent().clone(), file.getContent());
    }

    @Test
    public void testDeleteMany() {
        List<UUID> noteIds = new ArrayList<>();
        String[] names = {"tmp1", "tmp2", "tmp3", "tmp4"};
        Directory parent = insertDirectory(em, new Directory.DirectoryBuilder()
                .name("tmpParent")
                .parentId(EDA_DIRECTORY_ID)
                .user(pepeUser)
        );
        for (String name : names) {
            Note note = insertNote(em, new Note.NoteBuilder()
                    .name(name)
                    .subjectId(EDA_ID)
                    .parentId(parent.getId())
                    .user(pepeUser)
                    .visible(true)
                    .category(Category.PRACTICE)
                    .fileType("jpg"));
            noteIds.add(note.getId());
        }
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parent.getId() + "'");
        noteDao.delete(noteIds, PEPE_ID);
        em.flush();
        int countPostDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parent.getId() + "'");
        assertEquals(4, countInserted);
        assertEquals(0, countPostDelete);
    }

    @Test
    public void testDeleteNote() {
        int countPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NOTES, "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'");
        boolean deleted = noteDao.delete(Collections.singletonList(PARCIAL_DINAMICA_FLUIDOS_NOTE_ID), PEPE_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
        assertEquals(1, countPrev);
        assertTrue(deleted);
    }

    @Test
    public void testCannotDeleteNote() {
        boolean deleted = noteDao.delete(Collections.singletonList(PARCIAL_DINAMICA_FLUIDOS_NOTE_ID), SAIDMAN_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
        assertFalse(deleted);
    }

//    @Test
//    public void testUpdateNote() {
//        String oldName = "oldName";
//        String newName = "newName";
//        String oldCategory = "practice";
//        String newCategory = "theory";
//        boolean oldVisible = true;
//        boolean newVisible = false;
//        UUID parentId = EDA_DIRECTORY_ID;
//        UUID userId = PEPE_ID;
//
//        UUID noteId = jdbcInsertNote(namedParameterJdbcTemplate, parentId, oldName, EDA_ID, userId, oldVisible, new byte[]{1, 2, 3}, oldCategory, "jpg");
//
//        Note note = new Note.NoteBuilder()
//                .id(noteId)
//                .name(newName)
//                .visible(newVisible)
//                .category(Category.valueOf(newCategory.toUpperCase()))
//                .build();
//        int oldCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
//        int newCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");
//        boolean success = noteDao.update(note, userId);
//
//        int oldCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
//        int newCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");
//        assertTrue(success);
//        assertEquals(1, oldCountPrev);
//        assertEquals(0, newCountPrev);
//        assertEquals(0, oldCountPost);
//        assertEquals(1, newCountPost);
//    }
//
//    @Test
//    public void testUpdateNoteNotOwner() {
//        UUID adminId = jdbcInsertAdmin(namedParameterJdbcTemplate, "admin@mail.com" , "admin", ING_MEC_ID, "es");
//        String oldName = "oldName";
//        String newName = "newName";
//        String oldCategory = "practice";
//        String newCategory = "theory";
//        boolean oldVisible = true;
//        boolean newVisible = false;
//        UUID parentId = EDA_DIRECTORY_ID;
//        UUID userId = PEPE_ID;
//
//        UUID noteId = jdbcInsertNote(namedParameterJdbcTemplate, parentId, oldName, EDA_ID, userId, oldVisible, new byte[]{1, 2, 3}, oldCategory, "jpg");
//
//        Note note = new Note.NoteBuilder()
//                .id(noteId)
//                .name(newName)
//                .visible(newVisible)
//                .category(Category.valueOf(newCategory.toUpperCase()))
//                .build();
//        int oldCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
//        int newCountPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");
//
//        boolean success = noteDao.update(note, adminId);
//
//        int oldCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + oldName + "' AND category = '" + oldCategory + "'");
//        int newCountPost = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "notes", "note_id = '" + noteId + "' AND note_name = '" + newName + "' AND category = '" + newCategory + "'");
//        assertFalse(success);
//        assertEquals(1, oldCountPrev);
//        assertEquals(0, newCountPrev);
//        assertEquals(1, oldCountPost);
//        assertEquals(0, newCountPost);
//    }

    @Test
    public void testGetReview() {
        UUID newUserId = jdbcInsertStudent(namedParameterJdbcTemplate, "new@mail.com", "new", ING_INF_ID, "es");
        insertReview(namedParameterJdbcTemplate, GUIA1EDA_NOTE_ID, newUserId, 4, "ta ok");

        Review review = noteDao.getReview(GUIA1EDA_NOTE_ID, newUserId);

        assertEquals(newUserId, review.getUser().getUserId());
        assertEquals(4, review.getScore());
        assertEquals("ta ok", review.getContent());
    }


    @Test
    public void testGetReviews() {
        List<Review> reviews = noteDao.getReviews(GUIA1EDA_NOTE_ID);
//        Note note = noteDao.getNoteById(GUIA1EDA_NOTE_ID, SAIDMAN_ID).orElseThrow(AssertionError::new);
//        Set<Review> reviews = note.getReviews();
        assertEquals(2, reviews.size());
//        assertEquals(PEPE_ID, reviews.get(0).getUser().getUserId());
        assertNotNull(reviews.stream().filter(r -> r.getUser().getUserId().equals(PEPE_ID)).findFirst().orElse(null));
    }

    @Test
    public void testLimitReviews() {
        UUID[] userIds = new UUID[20];
        for (int i = 0; i < 20; i++)  {
            userIds[i] = jdbcInsertStudent(namedParameterJdbcTemplate, "pepe" + i + "@itba.edu.ar", "", ING_INF_ID, "es");
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
        noteDao.createOrUpdateReview(notePublic, pepeUser , 5, "Muy buen apunte");
        em.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + notePublic.getId() + "' AND user_id = '" + pepeUser.getUserId() + "' AND score = 5"));
    }

    @Test
    public void testUpdateReview(){
        Note guiaEda = em.find(Note.class, GUIA1EDA_NOTE_ID);
        int countScore4 = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + pepeUser.getUserId() + "' AND score = 4");
        noteDao.createOrUpdateReview(guiaEda, pepeUser , 5, "Cacique in the JODA");
        em.flush();
        assertEquals(1, countScore4);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + pepeUser.getUserId() + "' AND score = 5"));
    }

    @Test
    public void testDeleteReview(){
        int countReview = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4");
        noteDao.deleteReview(GUIA1EDA_NOTE_ID, PEPE_ID);
        em.flush();
        assertEquals(1, countReview);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4"));
    }

}
