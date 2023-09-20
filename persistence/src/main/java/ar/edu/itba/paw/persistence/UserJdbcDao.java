package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Override
    public User create(final String email, final String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put(EMAIL, email);
        args.put(PASSWORD, password);
        UUID userId = (UUID) jdbcInsert.executeAndReturnKeyHolder(args).getKeys().get(USER_ID);
        return new User(userId, email);
    }

    //TODO: remove when users are implemented
    @Transactional
    @Override
    public User createIfNotExists(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT user_id, email FROM Users WHERE email = ?",
                    new Object[]{email},
                    ROW_MAPPER
            );
        } catch (EmptyResultDataAccessException e) {
            return create(email, "cacho");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", ROW_MAPPER, email).stream().findFirst();
    }
}
