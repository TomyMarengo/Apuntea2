package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.user.Image;
import ar.edu.itba.paw.models.user.Role;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static ar.edu.itba.paw.persistence.TestUtils.*;
import static ar.edu.itba.paw.models.NameConstants.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class UserJpaDaoTest {
    @Autowired
    private UserJpaDao userDao;
    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() {
    }

    @Test
    public void testCreate() {
        String userEmail = "user@mail.com";
        userDao.create(userEmail, "", em.getReference(Career.class, ING_INF_ID), "es", Collections.singleton(Role.ROLE_STUDENT));
        em.flush();
        assertEquals(1, countRows(em, "users", "email = '" + userEmail + "'"));
        assertEquals(1, countRows(em, "users", "email = '" + userEmail + "' AND career_id = '" + ING_INF_ID + "' AND locale = 'es'"));
        assertEquals(1, countRows(em, "user_roles", "user_id = (SELECT user_id FROM users WHERE email = '" + userEmail + "') AND role_name = 'ROLE_STUDENT'"));
    }

    @Test
    public void testFindUserByEmail() {
        String email = "tester@itba.edu.ar";
        User student = insertStudent(em, email, "", ING_INF_ID, "es");
        Optional<User> maybeUser = userDao.findByEmail(email);

        User user = maybeUser.orElseThrow(AssertionError::new);
        assertEquals(student.getUserId(), user.getUserId());
        assertEquals(email, user.getEmail());
        assertEquals(ING_INF_ID, user.getCareer().getCareerId());
        assertEquals("es", user.getLocale().getLanguage());
    }

    @Test
    public void testFindUserByEmailNotFound() {
        String email = "tester@itba.edu.ar";
        Optional<User> maybeUser = userDao.findByEmail(email);
        assertEquals(Optional.empty(), maybeUser);
    }

    @Test
    public void testFindUserByUsername() {
        String username = "tester";
        User student = insertUser(em, new User.UserBuilder()
                .email("tester@itba.edu.ar")
                .username(username)
                .password("")
                .career(em.find(Career.class, ING_INF_ID))
                .locale("es")
                .roles(Collections.singleton(Role.ROLE_STUDENT))
                .firstName("Test")
                .lastName("Er"));

        Optional<User> maybeUser = userDao.findByUsername(username);

        User user = maybeUser.orElseThrow(AssertionError::new);
        assertEquals(student.getUserId(), user.getUserId());
        assertEquals(username, user.getUsername());
        assertEquals(ING_INF_ID, user.getCareer().getCareerId());
        assertEquals("es", user.getLocale().getLanguage());
        assertEquals("Test", user.getFirstName());
        assertEquals("Er", user.getLastName());
    }

    @Test
    public void testFindUserByUsernameNotFound() {
        String username = "tester";
        Optional<User> maybeUser = userDao.findByUsername(username);
        assertEquals(Optional.empty(), maybeUser);
    }

    @Test
    public void testFindUserById(){
        String email = "test@itba.edu.ar";
        User student = insertStudent(em, email, "", ING_INF_ID, "es");

        Optional<User> maybeUser = userDao.findById(student.getUserId());

        User user = maybeUser.orElseThrow(AssertionError::new);
        assertEquals(student, user);
        assertEquals(email, user.getEmail());
        assertEquals(ING_INF_ID, user.getCareer().getCareerId());
        assertEquals("es", user.getLocale().getLanguage());
    }

    @Test
    public void testFindUserByIdNotFound() {
        UUID userId = UUID.randomUUID();
        Optional<User> maybeUser = userDao.findById(userId);
        assertEquals(Optional.empty(), maybeUser);
    }

    @Test
    public void testBanUser() {
        User student = insertStudent(em, "student@mail.com", "", ING_INF_ID, "es");
        User admin = insertAdmin(em, "admin@mail.com", "", ING_INF_ID, "es");
        userDao.banUser(student, admin, LocalDateTime.now().plusDays(10), "Se porto mal");
        em.flush();
        assertEquals(1, countRows(em, "users", "user_id = '" + student.getUserId() + "' AND status = 'BANNED'"));
        assertEquals(1, countRows(em, "bans", "user_id = '" + student.getUserId() + "' AND admin_id = '" + admin.getUserId() + "'"));
    }

    @Test
    public void testUnbanUser() {
        User student = insertStudent(em, "student@mail.com", "", ING_INF_ID, "es");
        User admin = insertAdmin(em, "admin@mail.com", "", ING_INF_ID, "es");
        int firstQtyBanned = countRows(em, "users", "status = 'BANNED' AND user_id = '" + student.getUserId() + "'");
        banUser(em, student, admin, "", LocalDateTime.now().plusDays(10));
        int secondQtyBanned = countRows(em, "users", "status = 'BANNED' AND user_id = '" + student.getUserId() + "'");
        userDao.unbanUser(student);
        em.flush();
        int thirdQtyBanned = countRows(em, "users", "status = 'BANNED' AND user_id = '" + student.getUserId() + "'");
        assertEquals(0, firstQtyBanned);
        assertEquals(1, secondQtyBanned);
        assertEquals(0, thirdQtyBanned);
        assertEquals(0, countRows(em, "users", "user_id = '" + student.getUserId() + "' AND status = 'BANNED'"));
        assertEquals(1, countRows(em, "bans", "user_id = '" + student.getUserId() + "' AND admin_id = '" + admin.getUserId() + "'"));
    }

    @Test
    public void testUnbanUsers() {
        User admin = insertAdmin(em, "admin@mail.com", "", ING_INF_ID, "es");
        User[] students = new User[6];
        for (int i = 0; i < 5; i++) {
            students[i] = insertStudent(em, "student" + i + "@mail.com", "", ING_INF_ID, "es");
            banUser(em, students[i], admin, "...", LocalDateTime.now().minusDays(10));
        }
        students[5] = insertStudent(em, "student5@mail.com", "", ING_INF_ID, "es");
        banUser(em, students[5], admin, "", LocalDateTime.now().plusDays(10));
        int oldQtyBanned = countRows(em, "users", "status = 'BANNED'");
        userDao.unbanUsers();
        em.flush();
        assertEquals(6, oldQtyBanned);
        assertEquals(1, countRows(em, "users", "status = 'BANNED'"));
        assertEquals(1, countRows(em, "users", "user_id = '" + students[5].getUserId() + "' AND status = 'BANNED'"));
        for (int i = 0; i < 5; i++)
            assertEquals(1, countRows(em, "users", "user_id = '" + students[i].getUserId() + "' AND status = 'ACTIVE'"));
    }


    @Test
    public void testUpdateProfilePictureNoPicture(){
        User student = insertStudent(em, "student@mail.com", "", ING_INF_ID, "es");
        Image newImage = new Image(new byte[]{9, 8, 7, 6, 5, 4, 3, 2, 1});
        userDao.updateProfilePicture(student, newImage);
        em.flush();
        assertEquals(1, countRows(em, "users", "user_id = '" + student.getUserId() + "' AND profile_picture_id = '" + newImage.getImageId() + "'"));
    }

    @Test
    public void testUpdateProfilePictureHadPicture(){
        User student = insertStudent(em, "student@mail.com", "", ING_INF_ID, "es");
        Image oldImage = new Image(new byte[]{9, 8, 7, 6, 5, 4, 3, 2, 1});
        insertImage(em, oldImage, student);
        Image newImage = new Image(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        userDao.updateProfilePicture(student, newImage);
        em.flush();
        assertEquals(0, countRows(em, "users", "user_id = '" + student.getUserId() + "' AND profile_picture_id = '" + oldImage.getImageId() + "'"));
        assertEquals(1, countRows(em, "users", "user_id = '" + student.getUserId() + "' AND profile_picture_id IS NOT NULL"));
        assertEquals(1,countRows(em, "images", "image_id = '" + newImage.getImageId() + "'"));
        assertEquals(0, countRows(em, "images", "image_id = '" + oldImage.getImageId() + "'"));
    }

    @Test
    public void testGetStudents2000Count() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("t2000", null);
        assertEquals(0, results);
    }

    @Test
    public void testGetStudents20Count() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("t20", null);
        assertEquals(1, results);
    }

    @Test
    public void testGetStudents2Count() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("t2", null);
        assertEquals(2, results);
    }

    @Test
    public void testGetStudentsAllCount() {
        final int STUDENTS_LENGTH = 20;
        int oldUsers = countRows(em, USER_ROLES, "role_name = 'ROLE_STUDENT'"); // TODO: Change for admin students
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("", null);
        assertEquals(oldUsers + STUDENTS_LENGTH, results);
    }

    @Test
    public void testGetStudents2000() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        List<User> users = userDao.getStudents("t2000", null,1, 10);
        assertEquals(0, users.size());

    }

    @Test
    public void testGetStudents20() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        List<User> users = userDao.getStudents("t20", null, 1, 10);
        assertEquals(1, users.size());
        assertEquals("student20@mail.com", users.get(0).getEmail());
    }

    @Test
    public void testGetStudents2() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        List<User> users = userDao.getStudents("t2", null, 1, 10);
        assertEquals(2, users.size());
        assertEquals("student20@mail.com", users.get(0).getEmail());
        assertEquals("student2@mail.com", users.get(1).getEmail());
    }

    @Test
    public void testGetStudentsAll() {
        final int STUDENTS_LENGTH = 20;
        int oldUsers = countRows(em, USER_ROLES, "role_name = 'ROLE_STUDENT'"); // TODO: Change for admin students
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        // 100 should be more than enough to get all students, change in the future if necessary
        List<User> users = userDao.getStudents("", null, 1, 100);
        assertEquals(oldUsers + STUDENTS_LENGTH, users.size());

        for (int i = 0; i < STUDENTS_LENGTH; i++) {
            final int finalI = i;
            assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("student" + (finalI + 1) + "@mail.com")));
        }
    }

    @Test
    public void followTest() {
        User follower1 = insertStudent(em, "student1@mail.com", "", ING_INF_ID, "es");
        User student = insertStudent(em, "producer@mail.com", "", ING_INF_ID, "es");
        userDao.follow(follower1, student.getUserId());
        em.flush();
        assertEquals(1, countRows(em, "follows", "follower_id = '" + follower1.getUserId() + "' AND followed_id = '" + student.getUserId() + "'"));
    }

    @Test
    public void unfollowTest() {
        User follower1 = insertStudent(em, "student1@mail.com", "", ING_INF_ID, "es");
        User student = insertStudent(em, "producer@mail.com", "", ING_INF_ID, "es");
        insertFollower(em, follower1, student);
        // TODO: Check that the user had been followed

        userDao.unfollow(follower1, student.getUserId());
        em.flush();
        assertEquals(0, countRows(em, "follows", "follower_id = '" + follower1.getUserId() + "' AND followed_id = '" + student.getUserId() + "'"));

    }

    @Test
    public void testGetAvgScore() {
        User user = insertStudent(em, "user@itba.edu.ar", "1234", ING_INF_ID, "es");
        Note.NoteBuilder nb = new Note.NoteBuilder().user(user).parentId(EDA_DIRECTORY_ID).subject(em.getReference(Subject.class, EDA_ID)).category(Category.OTHER).fileType("pdf").visible(true);
        Note note1 = insertNote(em, nb.name("n1"));
        Note note2 = insertNote(em, nb.name("n2"));
        Note note3 = insertNote(em, nb.name("n3"));
        Note note4 = insertNote(em, nb.name("n4"));
        insertNote(em, nb.name("n5"));
        User pepeUser = em.getReference(User.class, PEPE_ID), carlaUser = em.getReference(User.class, CARLADMIN_ID), saidmanUser = em.getReference(User.class, SAIDMAN_ID);
        insertReview(em, note1, pepeUser, 5, "gud");
        insertReview(em, note1, carlaUser, 2, "git gud");
        insertReview(em, note1, saidmanUser, 4, "tabien");
        insertReview(em, note2, pepeUser, 5, "nice");
        insertReview(em, note3, pepeUser, 5, "cool");
        insertReview(em, note4, pepeUser, 5, "bein");
        float expected = (5 + 2 + 4 + 5 + 5 + 5) *1f / 6;

        float avgScore = userDao.getAvgScore(user.getUserId());
        em.flush();
        assertEquals(expected, avgScore, 0.1);
    }

    @Test
    public void testGetAvgScoreNoNotes() {
        User user = insertStudent(em, "user@itba.edu.ar", "1234", ING_INF_ID, "es");
        float avgScore = userDao.getAvgScore(user.getUserId());
        em.flush();
        assertEquals(0, avgScore, 0.0001);
        assertEquals(0, countRows(em, "notes", "user_id = '" + user.getUserId() + "'"));
    }

    @Test
    public void testGetAvgScoreNoReviews() {
        User user = insertStudent(em, "user@itba.edu.ar", "1234", ING_INF_ID, "es");
        Note.NoteBuilder nb = new Note.NoteBuilder().user(user).parentId(EDA_DIRECTORY_ID).subject(em.getReference(Subject.class, EDA_ID)).category(Category.OTHER).fileType("pdf").visible(true);
        insertNote(em, nb.name("n1"));
        insertNote(em, nb.name("n2"));
        float avgScore = userDao.getAvgScore(user.getUserId());
        em.flush();
        assertEquals(0, avgScore, 0.0001);
        assertEquals(2, countRows(em, "notes", "user_id = '" + user.getUserId() + "'"));
    }


}

