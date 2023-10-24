package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.VerificationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Repository
public class VerificationCodeJpaDao implements VerificationCodeDao {

    @PersistenceContext
    private EntityManager em;

    private final Logger LOGGER = LoggerFactory.getLogger(VerificationCodeJpaDao.class);

    @Override
    public VerificationCode saveVerificationCode(String code, User user, LocalDateTime expirationDate) {
        VerificationCode verificationCode = new VerificationCode(code, user, expirationDate);
        em.persist(verificationCode);
        return verificationCode;
    }

    @Override
    public boolean verifyForgotPasswordCode(String email, String code) {
        return ((BigInteger)em.createNativeQuery("SELECT COUNT(*) FROM Verification_Codes WHERE user_id = (SELECT user_id FROM Users WHERE email = :email) AND code = :code AND expires_at > NOW()")
                .setParameter("email", email)
                .setParameter("code", code)
                .getSingleResult()).intValue() > 0;
    }

    @Override
    public boolean deleteVerificationCodes(String email) {
        int qtyRemoved = em.createNativeQuery("DELETE FROM Verification_Codes WHERE user_id = (SELECT user_id FROM Users WHERE email = :email)")
                .setParameter("email", email)
                .executeUpdate();
        LOGGER.info("Deleted {} verification codes for user {}", qtyRemoved, email);
        return qtyRemoved > 0;
    }

    @Override
    public void removeExpiredCodes() {
        int qtyRemoved = em.createNativeQuery("DELETE FROM Verification_Codes WHERE expires_at < NOW()").executeUpdate();
        LOGGER.info("Deleted {} expired verification codes", qtyRemoved);
    }
}
