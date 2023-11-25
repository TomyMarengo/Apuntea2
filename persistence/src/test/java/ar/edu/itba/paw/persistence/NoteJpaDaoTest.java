package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static ar.edu.itba.paw.persistence.TestUtils.*;
import static ar.edu.itba.paw.models.NameConstants.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    private NoteJpaDao noteDao;
    private User pepeUser;
    private User saidmanUser;
    private User carlaAdmin;
    private Note notePublic;
    private Subject edaSubject;
    private byte[] publicContent;
    private Note notePrivate;
    private byte[] privateContent;
    @Before
    public void setUp() {
        pepeUser = em.find(User.class, PEPE_ID);
        saidmanUser = em.find(User.class, SAIDMAN_ID);
        carlaAdmin = em.find(User.class, CARLADMIN_ID);
        edaSubject = em.find(Subject.class, EDA_ID);
        Note.NoteBuilder builder = new Note.NoteBuilder()
                .name("public")
                .subject(edaSubject)
                .parentId(EDA_DIRECTORY_ID)
                .user(pepeUser)
                .visible(true)
                .category(Category.PRACTICE)
                .fileType("jpg");
        publicContent = new byte[]{1, 2, 3};
        notePublic = insertNote(em, builder, publicContent);
        builder.name("private").visible(false);
        privateContent = new byte[]{4, 5, 6};
        notePrivate = insertNote(em, builder, privateContent);
    }

    @Test
    public void testCreateNoteInDirectory() {
        UUID noteId = noteDao.create("RBT", EDA_ID, pepeUser, EDA_DIRECTORY_ID,true, new byte[]{1, 2, 3}, "PRACTICE", "jpg");
        em.flush();
        assertEquals(1, countRows(em, "notes", "note_name = 'RBT' AND user_id = '" + PEPE_ID + "' AND subject_id = '" + EDA_ID + "' AND category = 'PRACTICE'"));
        assertEquals(1, countRows(em, "note_files", "note_id = '" + noteId + "'"));
    }

    @Test
    public void testGetNoteByIdPublic() {
        Note foundNote = noteDao.getNoteById(notePublic.getId(), SAIDMAN_ID).orElseThrow(AssertionError::new);

        assertEquals(notePublic, foundNote);
        assertEquals(notePublic.getName(), foundNote.getName());
        assertEquals(EDA_ID, foundNote.getSubject().getSubjectId());
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
        NoteFile realNoteFile = em.find(NoteFile.class, notePublic.getId());

        NoteFile foundFile = noteDao.getNoteFileById(notePublic.getId(), SAIDMAN_ID).orElseThrow(AssertionError::new);

        assertEquals(realNoteFile.getMimeType(), foundFile.getMimeType());
        assertArrayEquals(publicContent, foundFile.getContent());
    }

    @Test
    public void testGetNoteFileByIdPrivate() {
        Optional<NoteFile> maybeFile = noteDao.getNoteFileById(notePrivate.getId(), carlaAdmin.getUserId());

        assertFalse(maybeFile.isPresent());
    }

    @Test
    public void testGetNoteFileByIdPrivateOwner() {
        NoteFile realNoteFile = em.find(NoteFile.class, notePrivate.getId());

        NoteFile file = noteDao.getNoteFileById(notePrivate.getId(), PEPE_ID).orElseThrow(AssertionError::new);

        assertEquals(realNoteFile.getMimeType(), file.getMimeType());
        assertArrayEquals(privateContent, file.getContent());
    }

    @Test
    public void testDeleteMany() {
        List<UUID> noteIds = new ArrayList<>();
        String[] names = {"tmp1", "tmp2", "tmp3", "tmp4"};
        Directory parent = insertDirectory(em, new Directory.DirectoryBuilder()
                .name("tmpParent")
                .parent(em.getReference(Directory.class, EDA_DIRECTORY_ID))
                .user(pepeUser)
        );
        for (String name : names) {
            Note note = insertNote(em, new Note.NoteBuilder()
                    .name(name)
                    .subject(edaSubject)
                    .parentId(parent.getId())
                    .user(pepeUser)
                    .visible(true)
                    .category(Category.PRACTICE)
                    .fileType("jpg"));
            noteIds.add(note.getId());
        }
        int countInserted = countRows(em, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parent.getId() + "'");
        noteDao.delete(noteIds, PEPE_ID);
        em.flush();
        int countPostDelete = countRows(em, NOTES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parent.getId() + "'");
        assertEquals(4, countInserted);
        assertEquals(0, countPostDelete);
    }

    @Test
    public void testDeleteNote() {
        int countPrev = countRows(em, NOTES, "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'");
        boolean deleted = noteDao.delete(Collections.singletonList(PARCIAL_DINAMICA_FLUIDOS_NOTE_ID), PEPE_ID);
        assertEquals(0, countRows(em, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
        assertEquals(1, countPrev);
        assertTrue(deleted);
    }

    @Test
    public void testCannotDeleteNote() {
        boolean deleted = noteDao.delete(Collections.singletonList(PARCIAL_DINAMICA_FLUIDOS_NOTE_ID), SAIDMAN_ID);
        assertEquals(1, countRows(em, "notes", "note_id = '" + PARCIAL_DINAMICA_FLUIDOS_NOTE_ID + "'"));
        assertFalse(deleted);
    }

    @Test
    public void testGetReview() {
        User newUser = insertStudent(em, "new@mail.com", "new", ING_INF_ID, "es");
        Note guiaEda = em.find(Note.class, GUIA1EDA_NOTE_ID);
        insertReview(em, guiaEda, newUser, 4, "ta ok");

        Review review = noteDao.getReview(guiaEda.getId(), newUser.getUserId());

        assertEquals(newUser, review.getUser());
        assertEquals(guiaEda, review.getNote());
        assertEquals(4, review.getScore());
        assertEquals("ta ok", review.getContent());
    }

    @Test
    public void testCountReviews() {
        int count = noteDao.countReviews(GUIA1EDA_NOTE_ID);
        assertEquals(2, count);
    }

    @Test
    public void testGetReviews() {
        List<Review> reviews = noteDao.getReviews(GUIA1EDA_NOTE_ID, 1);
        assertEquals(2, reviews.size());
        assertNotNull(reviews.stream().filter(r -> r.getUser().getUserId().equals(PEPE_ID)).findFirst().orElse(null));
    }

    private void spamReviews(UUID noteId) {
        int botCount = 15;
        Note guiaEda = em.find(Note.class, noteId);
        for (int i=0; i<botCount; i++) {
            User trollUser = insertStudent(em, "troll" + i + "@mail.com", "new" + i, ING_INF_ID, "es");
            insertReview(em, guiaEda, trollUser, 1, "malisimo "+i);
        }
    }

    @Test
    public void testLimitPaginatedReviews() {
        spamReviews(GUIA1EDA_NOTE_ID);
        int pageSize = 5;
        List<Review> reviews = noteDao.getReviews(GUIA1EDA_NOTE_ID, 2, pageSize);
        assertEquals(pageSize, reviews.size());
        for (int i=0; i<reviews.size()-2; i++) {
            assertFalse(reviews.get(i).getCreatedAt().isBefore(reviews.get(i+1).getCreatedAt()));
        }
    }

    @Test
    public void testLimitReviews() {
        spamReviews(GUIA1EDA_NOTE_ID);

        List<Review> reviews = noteDao.getFirstReviews(GUIA1EDA_NOTE_ID, PEPE_ID);
        assertEquals(NoteJpaDao.REVIEW_LIMIT, reviews.size());
        for (int i=1; i<reviews.size()-2; i++) {
            assertFalse(reviews.get(i).getCreatedAt().isBefore(reviews.get(i+1).getCreatedAt()));
        }
        assertEquals(pepeUser.getUserId(), reviews.get(0).getUser().getUserId());
    }

    @Test
    public void testLimitReviewsNoCurrentUserReview() {
        spamReviews(GUIA1EDA_NOTE_ID);

        List<Review> reviews = noteDao.getFirstReviews(GUIA1EDA_NOTE_ID, SAIDMAN_ID);
        assertEquals(NoteJpaDao.REVIEW_LIMIT, reviews.size());
        for (int i=1; i<reviews.size()-2; i++) {
            assertFalse(reviews.get(i).getCreatedAt().isBefore(reviews.get(i+1).getCreatedAt()));
        }
        assertNotEquals(saidmanUser.getUserId(), reviews.get(0).getUser().getUserId());
        assertTrue(reviews.stream().noneMatch(r -> r.getUser().getUserId().equals(saidmanUser.getUserId())));
    }


    @Test
    public void testCreateReview(){
        noteDao.createOrUpdateReview(notePublic, pepeUser, 5, "Muy buen apunte");
        em.flush();
        assertEquals(1, countRows(em, "reviews", "note_id = '" + notePublic.getId() + "' AND user_id = '" + pepeUser.getUserId() + "' AND score = 5"));
    }

    @Test
    public void testUpdateReview(){
        Note guiaEda = em.find(Note.class, GUIA1EDA_NOTE_ID);
        int countScore4 = countRows(em, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + pepeUser.getUserId() + "' AND score = 4");
        noteDao.createOrUpdateReview(guiaEda, pepeUser, 5, "Cacique in the JODA");
        em.flush();
        assertEquals(1, countScore4);
        assertEquals(1, countRows(em, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + pepeUser.getUserId() + "' AND score = 5"));
    }

    @Test
    public void testDeleteReview(){
        int countReview = countRows(em, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4");
        noteDao.deleteReview(GUIA1EDA_NOTE_ID, PEPE_ID);
        em.flush();
        assertEquals(1, countReview);
        assertEquals(0, countRows(em, "reviews", "note_id = '" + GUIA1EDA_NOTE_ID + "' AND user_id = '" + PEPE_ID + "' AND score = 4"));
    }

    @Test
    public void testAddFavorite() {
        noteDao.addFavorite(em.getReference(User.class, PEPE_ID), MVC_NOTE_ID);
        em.flush();
        assertEquals(1, countRows(em, "Note_Favorites", "user_id = '" + PEPE_ID + "' AND note_id = '" + MVC_NOTE_ID + "'"));
        assertEquals(0, countRows(em, "Note_Favorites", "user_id = '" + SAIDMAN_ID + "' AND note_id = '" + MVC_NOTE_ID + "'"));
    }

    @Test
    public void testRemoveFavorite() {
        insertFavoriteNote(em, notePublic.getId(), PEPE_ID);
        insertFavoriteNote(em, notePublic.getId(), SAIDMAN_ID);
        noteDao.removeFavorite(em.getReference(User.class, PEPE_ID), notePublic.getId());
        em.flush();
        assertEquals(0, countRows(em, "Note_Favorites", "user_id = '" + PEPE_ID + "' AND note_id = '" + notePublic.getId() + "'"));
        assertEquals(1, countRows(em, "Note_Favorites", "user_id = '" + SAIDMAN_ID + "' AND note_id = '" + notePublic.getId() + "'"));
    }

    @Test
    public void testCountReviewsUser() {
        int count = noteDao.countReviewsByUser(PEPE_ID);
        assertEquals(4, count);
    }

    @Test
    public void testGetReviewsByUserInterference() {
        Note newNote = insertNote(em, new Note.NoteBuilder()
                .name("new")
                .subject(edaSubject)
                .parentId(EDA_DIRECTORY_ID)
                .user(saidmanUser)
                .visible(true)
                .category(Category.PRACTICE)
                .fileType("jpg"));
        insertReview(em, newNote, carlaAdmin, 4, "ta ok!");

        List<Review> reviews = noteDao.getReviewsByUser(PEPE_ID, 1, 10);

        assertEquals(4, reviews.size());
        assertEquals(2, reviews.stream().filter(r -> r.getNote().getId().equals(GUIA1EDA_NOTE_ID)).count());
        assertEquals(1, reviews.stream().filter(r -> r.getNote().getId().equals(LUCENE_NOTE_ID)).count());
        assertEquals(1, reviews.stream().filter(r -> r.getNote().getId().equals(JAVA_BEANS_NOTE_ID)).count());
    }

    @Test
    public void testGetReviewsByUserPagination() {
        Note newNote = insertNote(em, new Note.NoteBuilder()
                .name("new")
                .subject(edaSubject)
                .parentId(EDA_DIRECTORY_ID)
                .user(pepeUser)
                .visible(true)
                .category(Category.PRACTICE)
                .fileType("jpg"));
        insertReview(em, newNote, carlaAdmin, 4, "ta ok!");

        List<Review> reviews = noteDao.getReviewsByUser(PEPE_ID, 1, 4);

        assertEquals(4, reviews.size());
        assertEquals("new", reviews.get(0).getNote().getName());
        for (int i=0; i<reviews.size()-2; i++) {
            assertFalse(reviews.get(i).getCreatedAt().isBefore(reviews.get(i+1).getCreatedAt()));
        }
    }

}
