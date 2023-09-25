package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcRoleInsert;


    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum)  ->
            new User(
                    UUID.fromString(rs.getString(USER_ID)),
                    rs.getString(EMAIL),
                    rs.getString(PASSWORD),
                    (String[]) rs.getArray(ROLES).getArray()
            );

    @Autowired
    public UserJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcRoleInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_ROLES);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    // TODO: Make transactional in 3rd sprint
//    @Transactional
    @Override
    public void create(final String email, final String password, final UUID institutionId, final UUID careerId, final String lang, final Role role){
        final MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);
        args.addValue(PASSWORD, password);
        args.addValue(INSTITUTION_ID, institutionId);
        args.addValue(CAREER_ID, careerId);
        args.addValue(LOCALE, lang);

        KeyHolder holder = new GeneratedKeyHolder();

        // TODO: See if we can remove the try catch for the last sprint
        try {
            namedParameterJdbcTemplate.update("INSERT INTO users (email, password, institution_id, career_id, locale) VALUES (:email, :password, :institution_id, :career_id, :locale)",
                    args, holder, new String[]{USER_ID} );
        } catch (DuplicateKeyException e) {
            int rows = namedParameterJdbcTemplate.update("UPDATE users SET password = :password, institution_id = :institution_id, career_id = :career_id, locale = :locale WHERE email = :email AND password IS NULL",
                    args, holder, new String[]{USER_ID} );
            if (rows == 0) throw e;
        }

        final Map<String, Object> roleArgs = new HashMap<>();
        roleArgs.put(USER_ID, holder.getKeys().get(USER_ID));
        roleArgs.put(ROLE_NAME, role.getRole());
        jdbcRoleInsert.execute(roleArgs);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> optionalUser = jdbcTemplate.query("SELECT u.*, array_agg(r.role_name) as roles FROM users u INNER JOIN User_Roles r ON u.user_id = r.user_id WHERE email = ? " +
                "GROUP BY u.user_id", ROW_MAPPER, email).stream().findFirst();
        return optionalUser;
    }
}
