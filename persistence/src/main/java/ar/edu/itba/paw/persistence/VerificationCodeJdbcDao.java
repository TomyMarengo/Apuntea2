package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.VerificationCode;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class VerificationCodeJdbcDao implements VerificationCodeDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(VerificationCodeJdbcDao.class);

    @Autowired
    public VerificationCodeJdbcDao(final DataSource ds) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public boolean saveVerificationCode(VerificationCode verificationCode) {
        return this.namedParameterJdbcTemplate.update(
                "INSERT INTO Verification_Codes(user_id, code, expires_at) values(" +
                        "(SELECT user_id FROM Users WHERE email = :email), " +
                        ":code, :expires_at)",
                new MapSqlParameterSource()
                        .addValue("email", verificationCode.getEmail())
                        .addValue("code", verificationCode.getCode())
                        .addValue("expires_at", verificationCode.getExpirationDate())
        ) == 1;
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
        LOGGER.debug("Deleted {} verification codes for user {}", qtyRemoved, email);
        return qtyRemoved > 0;
    }

    // TODO: Cron to remove expired codes
    @Override
    public void removeExpiredCodes() {
        int qtyRemoved = this.jdbcTemplate.update("DELETE FROM Verification_Codes WHERE expires_at < NOW()");
        LOGGER.debug("Deleted {} expired verification codes", qtyRemoved);
    }
}
