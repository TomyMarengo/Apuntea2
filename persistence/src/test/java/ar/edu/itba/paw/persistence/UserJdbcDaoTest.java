package ar.edu.itba.paw.persistence;

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

import javax.sql.DataSource;

import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class UserJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private UserJdbcDao userDao;
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
        userDao.create(userEmail, "", ING_INF , "es", Role.ROLE_STUDENT);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "email = '" + userEmail + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "email = '" + userEmail + "' AND career_id = '" + ING_INF + "' AND locale = 'es'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_roles", "user_id = (SELECT user_id FROM users WHERE email = '" + userEmail + "') AND role_name = 'ROLE_STUDENT'"));
    }

    @Test
    public void testCreateDonofrio() { // Retro-compatibility with sprint 1 users
        String userEmail = "ndonofri@itba.edu.ar";
        insertLegacyUser(namedParameterJdbcTemplate, userEmail);
        userDao.create(userEmail, "1234", ING_MEC , "es", Role.ROLE_STUDENT);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "email = '" + userEmail + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "email = '" + userEmail + "' AND password IS NOT NULL"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_roles", "user_id = (SELECT user_id FROM users WHERE email = '" + userEmail + "') AND role_name = 'ROLE_STUDENT'"));
    }

    @Test
    public void testFindUserByEmail() {
        String email = "tester@itba.edu.ar";
        UUID studentId = insertStudent(namedParameterJdbcTemplate, email, "", ING_INF, "es");
        Optional<User> maybeUser = userDao.findByEmail(email);
        assertEquals(studentId, maybeUser.get().getUserId());
        assertEquals(email, maybeUser.get().getEmail());
        assertEquals(ING_INF, maybeUser.get().getCareer().getCareerId());
        assertEquals("es", maybeUser.get().getLocale());
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
        UUID studentId = insertCompleteStudent(namedParameterJdbcTemplate, username, "", ING_INF, "es", username, "Test", "Er");
        Optional<User> maybeUser = userDao.findByUsername(username);
        assertEquals(studentId, maybeUser.get().getUserId());
        assertEquals(username, maybeUser.get().getUsername());
        assertEquals(ING_INF, maybeUser.get().getCareer().getCareerId());
        assertEquals("es", maybeUser.get().getLocale());
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
    public void testUpdate() {
        UUID studentId = insertCompleteStudent(namedParameterJdbcTemplate, "tester@itba.edu.ar", "", ING_INF, "es", "tester", "Test", "Er");
        User user = new User.UserBuilder().userId(studentId).firstName("Testa").lastName("Er").username("tester2001").build();
        boolean success = userDao.update(user);
        assertTrue(success);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + studentId + "' AND first_name = 'Testa' AND last_name = 'Er' AND username = 'tester2001'"));
    }

    @Test
    public void testUpdateFailure() {
        User user = new User.UserBuilder().userId(UUID.randomUUID()).firstName("Testa").lastName("Er").username("tester2001").build();
        boolean success = userDao.update(user);
        assertFalse(success);
    }

    @Test
    public void testBanUser() {
        UUID studentId = insertStudent(namedParameterJdbcTemplate, "student@mail.com", "", ING_INF, "es");
        UUID adminId = insertAdmin(namedParameterJdbcTemplate, "admin@mail.com", "", ING_INF, "es");
        userDao.banUser(studentId, adminId, LocalDateTime.now().plusDays(10), "Se porto mal");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + studentId + "' AND status = 'BANNED'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "bans", "user_id = '" + studentId + "' AND admin_id = '" + adminId + "'"));
    }

    @Test
    public void testUnbanUsers() {
        UUID adminId = insertAdmin(namedParameterJdbcTemplate, "admin@mail.com", "", ING_INF, "es");
        UUID[] studentIds = new UUID[6];
        for (int i = 0; i < 5; i++) {
            studentIds[i] = insertStudent(namedParameterJdbcTemplate, "student" + i + "@mail.com", "", ING_INF, "es");
            banUser(namedParameterJdbcTemplate, studentIds[i], adminId, LocalDateTime.now().minusDays(10));
        }
        studentIds[5] = insertStudent(namedParameterJdbcTemplate, "student5@mail.com", "", ING_INF, "es");
        banUser(namedParameterJdbcTemplate, studentIds[5], adminId, LocalDateTime.now().plusDays(10));
        int oldQtyBanned = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "status = 'BANNED'");
        userDao.unbanUsers();
        assertEquals(6, oldQtyBanned);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "status = 'BANNED'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + studentIds[5] + "' AND status = 'BANNED'"));
        for (int i = 0; i < 5; i++)
            assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + studentIds[i] + "' AND status = 'ACTIVE'"));
    }

    // TODO: Test profile picture methods?

    @Test
    public void testGetStudents2000Count() {
        int currentUsers = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        assertEquals(0, userDao.getStudentsQuantity("t2000"));
    }

    @Test
    public void testGetStudents20Count() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        assertEquals(1, userDao.getStudentsQuantity("t20"));
    }

    @Test
    public void testGetStudents2Count() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        assertEquals(2, userDao.getStudentsQuantity("t2"));
    }

    @Test
    public void testGetStudentsAllCount() {
        final int STUDENTS_LENGTH = 20;
        int oldUsers = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_ROLES, "role_name = 'ROLE_STUDENT' AND NOT EXISTS ( SELECT * FROM User_Roles ur WHERE ur.user_id = user_id  AND ur.role_name = 'ROLE_ADMIN')");
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        assertEquals(oldUsers + STUDENTS_LENGTH, userDao.getStudentsQuantity(""));
    }

    @Test
    public void testGetStudents2000() {
        int currentUsers = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        List<User> users = userDao.getStudents("t2000", 1, 10);
        assertEquals(0, users.size());

    }

    @Test
    public void testGetStudents20() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        List<User> users = userDao.getStudents("t20", 1, 10);
        assertEquals(1, users.size());
        assertEquals("student20@mail.com", users.get(0).getEmail());
    }

    @Test
    public void testGetStudents2() {
        final int STUDENTS_LENGTH = 20;
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        List<User> users = userDao.getStudents("t2", 1, 10);
        assertEquals(2, users.size());
        assertEquals("student20@mail.com", users.get(0).getEmail());
        assertEquals("student2@mail.com", users.get(1).getEmail());
    }

    @Test
    public void testGetStudentsAll() {
        final int STUDENTS_LENGTH = 20;
        int oldUsers = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_ROLES, "role_name = 'ROLE_STUDENT' AND NOT EXISTS ( SELECT * FROM User_Roles ur WHERE ur.user_id = user_id  AND ur.role_name = 'ROLE_ADMIN')");
        for (int i = 0; i < STUDENTS_LENGTH; i++) insertStudent(namedParameterJdbcTemplate, "student" + (i + 1) + "@mail.com", "", ING_INF, "es");
        // 100 should be more than enough to get all students, change in the future if necessary
        List<User> users = userDao.getStudents("", 1, 100);
        assertEquals(oldUsers + STUDENTS_LENGTH, users.size());

        for (int i = 0; i < STUDENTS_LENGTH; i++) {
            final int finalI = i;
            assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("student" + (finalI + 1) + "@mail.com")));
        }
    }
}

