package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
                    UUID.fromString(rs.getString("user_id")),
                    rs.getString("email")
            );

    @Autowired
    public UserJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Users")
                .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public User create(final String email) {
        final Map<String, Object> args = new HashMap<>();
        args.put("email", email);
        jdbcInsert.execute(args);
        return new User(email);
    }

    @Override
    public Optional<User> findById(final long id) {
        return jdbcTemplate.query("SELECT * FROM Users WHERE user_id = ?",
                new Object[]{id}, ROW_MAPPER).stream().findFirst();
    }

    @Transactional
    @Override
    public UUID getUserIdByEmail(String email, UUID institutionId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT user_id FROM Users WHERE email = ?",
                    new Object[]{email},
                    (rs, rowNum) -> UUID.fromString(rs.getString("user_id"))
            );
        } catch (EmptyResultDataAccessException e) {
            String sql = "INSERT INTO Users (username, email, institution_id) VALUES (?, ?, ?) RETURNING user_id";
            Object[] params = {email, email, institutionId};
            return jdbcTemplate.queryForObject(
                    sql,
                    params,
                    (rs, rowNum) -> (UUID) rs.getObject("user_id")
            );
        }
    }
}
