package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.VerificationCode;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class VerificationCodeJdbcDao implements VerificationCodeDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(VerificationCodeJdbcDao.class);

    @Autowired
    public VerificationCodeJdbcDao(final DataSource ds) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void saveVerificationCode(VerificationCode verificationCode) {
        try  {
            // TODO: Remove last verification code for user (if exists)
            this.namedParameterJdbcTemplate.update(
                    "INSERT INTO Verification_Codes(user_id, code, expires_at) values(" +
                            "(SELECT user_id FROM Users WHERE email = :email), " +
                            ":code, :expires_at)",
                    new MapSqlParameterSource()
                            .addValue("email", verificationCode.getEmail())
                            .addValue("code", verificationCode.getCode())
                            .addValue("expires_at", verificationCode.getExpirationDate())
            );
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Invalid verification code: email does not exist {}", verificationCode.getEmail());
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean verifyForgotPasswordCode(String email, String code) {
        return this.namedParameterJdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM Verification_Codes WHERE user_id = (SELECT user_id FROM Users WHERE email = :email) AND code = :code AND expires_at > NOW())",
                new MapSqlParameterSource()
                        .addValue("email", email)
                        .addValue("code", code),
                Boolean.class
        );
    }

    @Override
    public boolean deleteVerificationCodes(String email) {
        int qtyRemoved = this.namedParameterJdbcTemplate.update(
                "DELETE FROM Verification_Codes WHERE user_id = (SELECT user_id FROM Users WHERE email = :email)",
                new MapSqlParameterSource()
                        .addValue("email", email));
        LOGGER.info("Deleted {} verification codes for user {}", qtyRemoved, email);
        return qtyRemoved > 0;
    }

    @Override
    public void removeExpiredCodes() {
        int qtyRemoved = this.jdbcTemplate.update("DELETE FROM Verification_Codes WHERE expires_at < NOW()");
        LOGGER.info("Deleted {} expired verification codes", qtyRemoved);
    }
}
