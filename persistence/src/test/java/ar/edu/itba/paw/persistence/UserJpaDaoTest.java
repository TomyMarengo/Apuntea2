package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.Image;
import ar.edu.itba.paw.models.user.Role;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

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
    private DataSource ds;
    @Autowired
    private UserJpaDao userDao;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    @Test
    public void testCreate() {
        String userEmail = "user@mail.com";
        userDao.create(userEmail, "", ING_INF, "es", Collections.singleton(Role.ROLE_STUDENT));
        em.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "email = '" + userEmail + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "email = '" + userEmail + "' AND career_id = '" + ING_INF_ID + "' AND locale = 'es'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_roles", "user_id = (SELECT user_id FROM users WHERE email = '" + userEmail + "') AND role_name = 'ROLE_STUDENT'"));
    }

    @Test
    public void testFindUserByEmail() {
        String email = "tester@itba.edu.ar";
        UUID studentId = jdbcInsertStudent(namedParameterJdbcTemplate, email, "", ING_INF_ID, "es");
        Optional<User> maybeUser = userDao.findByEmail(email);
        assertEquals(studentId, maybeUser.get().getUserId());
        assertEquals(email, maybeUser.get().getEmail());
        assertEquals(ING_INF_ID, maybeUser.get().getCareer().getCareerId());
        assertEquals("es", maybeUser.get().getLocale().getLanguage());
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
        UUID studentId = insertCompleteStudent(namedParameterJdbcTemplate, username, "", ING_INF_ID, "es", username, "Test", "Er");
        Optional<User> maybeUser = userDao.findByUsername(username);
        assertEquals(studentId, maybeUser.get().getUserId());
        assertEquals(username, maybeUser.get().getUsername());
        assertEquals(ING_INF_ID, maybeUser.get().getCareer().getCareerId());
        assertEquals("es", maybeUser.get().getLocale().getLanguage());
        assertEquals("Test", maybeUser.get().getFirstName());
        assertEquals("Er", maybeUser.get().getLastName());
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
        UUID studentId = jdbcInsertStudent(namedParameterJdbcTemplate, email, "", ING_INF_ID, "es");
        Optional<User> maybeUser = userDao.findById(studentId);
        assertEquals(studentId, maybeUser.get().getUserId());
        assertEquals(email, maybeUser.get().getEmail());
        assertEquals(ING_INF_ID, maybeUser.get().getCareer().getCareerId());
        assertEquals("es", maybeUser.get().getLocale().getLanguage());
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
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + student.getUserId() + "' AND status = 'BANNED'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "bans", "user_id = '" + student.getUserId() + "' AND admin_id = '" + admin.getUserId() + "'"));
    }

    @Test
    public void testUnbanUser() {
        User student = insertStudent(em, "student@mail.com", "", ING_INF_ID, "es");
        User admin = insertAdmin(em, "admin@mail.com", "", ING_INF_ID, "es");
        int firstQtyBanned = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "status = 'BANNED' AND user_id = '" + student.getUserId() + "'");
        banUser(em, student, admin, "", LocalDateTime.now().plusDays(10));
        int secondQtyBanned = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "status = 'BANNED' AND user_id = '" + student.getUserId() + "'");
        userDao.unbanUser(student);
        em.flush();
        int thirdQtyBanned = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "status = 'BANNED' AND user_id = '" + student.getUserId() + "'");
        assertEquals(0, firstQtyBanned);
        assertEquals(1, secondQtyBanned);
        assertEquals(0, thirdQtyBanned);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + student.getUserId() + "' AND status = 'BANNED'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "bans", "user_id = '" + student.getUserId() + "' AND admin_id = '" + admin.getUserId() + "'"));
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
        int oldQtyBanned = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "status = 'BANNED'");
        userDao.unbanUsers();
        em.flush();
        assertEquals(6, oldQtyBanned);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "status = 'BANNED'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + students[5].getUserId() + "' AND status = 'BANNED'"));
        for (int i = 0; i < 5; i++)
            assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + students[i].getUserId() + "' AND status = 'ACTIVE'"));
    }


    @Test
    public void testUpdateProfilePictureNoPicture(){
        User student = insertStudent(em, "student@mail.com", "", ING_INF_ID, "es");
        Image newImage = new Image(new byte[]{9, 8, 7, 6, 5, 4, 3, 2, 1});
        userDao.updateProfilePicture(student, newImage);
        em.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + student.getUserId() + "' AND profile_picture_id = '" + newImage.getImageId() + "'"));
    }

    @Test
    public void testUpdateProfilePictureHadPicture(){
        User student = insertStudent(em, "student@mail.com", "", ING_INF_ID, "es");
        Image oldImage = new Image(new byte[]{9, 8, 7, 6, 5, 4, 3, 2, 1});
        Image img = insertImage(em, oldImage, student);
        Image newImage = new Image(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        userDao.updateProfilePicture(student, newImage);
        em.flush();
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + student.getUserId() + "' AND profile_picture_id = '" + oldImage.getImageId() + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + student.getUserId() + "' AND profile_picture_id IS NOT NULL"));
        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "images", "image_id = '" + newImage.getImageId() + "'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "images", "image_id = '" + oldImage.getImageId() + "'"));
    }

    @Test
    public void testGetStudents2000Count() {
        int currentUsers = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) jdbcInsertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("t2000");
        assertEquals(0, results);
    }

    @Test
    public void testGetStudents20Count() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) jdbcInsertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("t20");
        assertEquals(1, results);
    }

    @Test
    public void testGetStudents2Count() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) jdbcInsertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("t2");
        assertEquals(2, results);
    }

    @Test
    public void testGetStudentsAllCount() {
        final int STUDENTS_LENGTH = 20;
        int oldUsers = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_ROLES, "role_name = 'ROLE_STUDENT'"); // TODO: Change for admin students
        for (int i = 0; i < STUDENTS_LENGTH; i++) jdbcInsertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        int results = userDao.getStudentsQuantity("");
        assertEquals(oldUsers + STUDENTS_LENGTH, results);
    }

    @Test
    public void testGetStudents2000() {
        int currentUsers = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) jdbcInsertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        List<User> users = userDao.getStudents("t2000", 1, 10);
        assertEquals(0, users.size());

    }

    @Test
    public void testGetStudents20() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) jdbcInsertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        List<User> users = userDao.getStudents("t20", 1, 10);
        assertEquals(1, users.size());
        assertEquals("student20@mail.com", users.get(0).getEmail());
    }

    @Test
    public void testGetStudents2() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        List<User> users = userDao.getStudents("t2", 1, 10);
        assertEquals(2, users.size());
        assertEquals("student20@mail.com", users.get(0).getEmail());
        assertEquals("student2@mail.com", users.get(1).getEmail());
    }

    @Test
    public void testGetStudentsAll() {
        final int STUDENTS_LENGTH = 20;
        int oldUsers = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_ROLES, "role_name = 'ROLE_STUDENT'"); // TODO: Change for admin students
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(em, "student" + (i + 1) + "@mail.com", "", ING_INF_ID, "es");
        // 100 should be more than enough to get all students, change in the future if necessary
        List<User> users = userDao.getStudents("", 1, 100);
        assertEquals(oldUsers + STUDENTS_LENGTH, users.size());

        for (int i = 0; i < STUDENTS_LENGTH; i++) {
            final int finalI = i;
            assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("student" + (finalI + 1) + "@mail.com")));
        }
    }
}

