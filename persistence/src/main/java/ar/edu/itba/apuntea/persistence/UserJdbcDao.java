package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
class UserJdbcDao implements UserDao{
    // Templetiza todo el borderplate para evitar la repetición
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    // Cómo es una interfaz no es necesaria que la cree cada vez que entro en un método
    // y es por eso que creo solamente una.
    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum)  ->
            new User(rs.getLong("userId"), rs.getString("email"), rs.getString("password"));

    @Autowired
    public UserJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("\"User\"")
                .usingGeneratedKeyColumns("\"userId\"");
    }

    @Override
    public User create(final long userId, final String email, final String password) {
        return new User(userId, email, password);
    }

    @Override
    public Optional<User> findById(final long id) {
        return jdbcTemplate.query("SELECT * FROM \"User\" WHERE \"userId\" = ?",
                new Object[]{id}, ROW_MAPPER).stream().findFirst();
    }
}
