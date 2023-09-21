package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserJdbcDao implements UserDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum)  ->
            new User(
                    UUID.fromString(rs.getString(USER_ID)),
                    rs.getString(EMAIL),
                    rs.getString(PASSWORD)
            );

    @Autowired
    public UserJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USERS)
                .usingGeneratedKeyColumns(USER_ID);
    }

    @Transactional
    @Override
    public void create(final String email, final String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put(EMAIL, email);
        args.put(PASSWORD, password);
        try {
            jdbcInsert.executeAndReturnKeyHolder(args).getKeys().get(USER_ID);
        } catch (DuplicateKeyException e) {
            int rows = jdbcTemplate.update("UPDATE users SET password = ? WHERE email = ? AND password IS NULL", password, email);
            if (rows == 0) throw e;
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", ROW_MAPPER, email).stream().findFirst();
    }
}
