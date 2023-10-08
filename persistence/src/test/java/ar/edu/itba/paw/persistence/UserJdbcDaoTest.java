package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
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
import static org.junit.Assert.*;

import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class UserJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private UserDao userDao;
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
        User user = new User("Testa", "Er", "tester2001");
        user.setUserId(studentId);
        boolean success = userDao.update(user);
        assertTrue(success);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "user_id = '" + studentId + "' AND first_name = 'Testa' AND last_name = 'Er' AND username = 'tester2001'"));
    }

    @Test
    public void testUpdateFailure() {
        User user = new User("Testa", "Er", "tester2001");
        user.setUserId(UUID.fromString("d0000000-0000-0000-0000-000000000000"));
        boolean success = userDao.update(user);
        assertFalse(success);
    }

    // TODO: Test profile picture methods?
}

