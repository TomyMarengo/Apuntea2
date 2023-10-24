package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.VerificationCode;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.time.LocalDateTime;

import static ar.edu.itba.paw.persistence.TestUtils.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

import org.springframework.test.jdbc.JdbcTestUtils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class VerificationCodeJpaDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private VerificationCodeJpaDao verificationCodeDao;

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private User user;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);

        user = insertStudent(em, VERIFICATION_EMAIL, "", ING_INF_ID, "es");
    }

    @Test
    public void testSaveVerificationCode() {
        verificationCodeDao.saveVerificationCode("123456", user, LocalDateTime.now().plusMinutes(10));
        em.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_codes", "user_id = '" + user.getUserId() + "' AND code = '123456' AND expires_at > NOW()"));
    }

    @Test
    public void testVerifyForgotPasswordCode() {
        insertVerificationCode(em, DEFAULT_CODE, user, LocalDateTime.now().plusMinutes(10));
        boolean success = verificationCodeDao.verifyForgotPasswordCode(VERIFICATION_EMAIL, DEFAULT_CODE);
        assertTrue(success);
    }

    @Test
    public void testVerifyForgotPasswordCodeInvalidCode() {
        insertVerificationCode(em, DEFAULT_CODE, user, LocalDateTime.now().plusMinutes(10));
        boolean success = verificationCodeDao.verifyForgotPasswordCode(VERIFICATION_EMAIL, DEFAULT_CODE2);
        assertFalse(success);
    }

    @Test
    public void testVerifyForgotPasswordCodeInvalidEmail() {
        boolean success = verificationCodeDao.verifyForgotPasswordCode(VERIFICATION_EMAIL, DEFAULT_CODE);
        assertFalse(success);
    }

    @Test
    public void testVerifyForgotPasswordExpired() {
        VerificationCode code = new VerificationCode(DEFAULT_CODE, user, LocalDateTime.now().minusMinutes(10));
        jdbcInsertVerificationCode(namedParameterJdbcTemplate, code);
        boolean success = verificationCodeDao.verifyForgotPasswordCode(code.getEmail(), DEFAULT_CODE);
        assertFalse(success);
    }

    @Test
    public void testDeleteVerificationCodes() {
        User otherUser = insertStudent(em, "bis" + VERIFICATION_EMAIL, "", ING_INF_ID, "es");
        for (int i = 0; i < 10; i++) {
            insertVerificationCode(em, "12345" + i, user, LocalDateTime.now().plusMinutes(10));
        }
        VerificationCode code = new VerificationCode(DEFAULT_CODE, otherUser, LocalDateTime.now().plusMinutes(10));
        insertVerificationCode(em, DEFAULT_CODE, otherUser, LocalDateTime.now().plusMinutes(10));
        int oldQty = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_codes", "user_id = (SELECT user_id FROM users WHERE email = '" + VERIFICATION_EMAIL + "')");
        boolean success = verificationCodeDao.deleteVerificationCodes(VERIFICATION_EMAIL);
        assertTrue(success);
        assertEquals(10, oldQty);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_codes", "user_id = (SELECT user_id FROM users WHERE email = '" + VERIFICATION_EMAIL + "')"));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "verification_codes"));
    }

    @Test
    public void testRemoveExpiredCodes() {
        String otherMail = "bis" + VERIFICATION_EMAIL;
        User otherUser = insertStudent(em, otherMail, "", ING_INF_ID, "es");
        for (int i = 0; i < 10; i++) {
            insertVerificationCode(em, "12345" + i, i % 2 == 0? user : otherUser, LocalDateTime.now().minusMinutes(10));
        }
        int oldQty1 = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_codes", "user_id = (SELECT user_id FROM users WHERE email = '" + VERIFICATION_EMAIL + "')");
        int oldQty2 = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_codes", "user_id = (SELECT user_id FROM users WHERE email = '" + otherMail + "')");
        int oldQty = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_codes", "expires_at < NOW()");
        verificationCodeDao.removeExpiredCodes();
        assertEquals(5, oldQty1);
        assertEquals(5, oldQty2);
        assertEquals(10, oldQty);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_codes", "expires_at < NOW()"));
    }
 }
