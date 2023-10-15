package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.user.ProfilePicture;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
class UserJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcRoleInsert;
    private final Logger LOGGER = LoggerFactory.getLogger(UserJdbcDao.class);

    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum)  -> {
        Object[] roles = (Object[]) rs.getArray(ROLES).getArray();
        String[] rolesString = Arrays.copyOf(roles, roles.length, String[].class);
        return new User.UserBuilder()
                .userId(UUID.fromString(rs.getString(USER_ID)))
                .firstName(rs.getString(FIRST_NAME))
                .lastName(rs.getString(LAST_NAME))
                .username(rs.getString(USERNAME))
                .email(rs.getString(EMAIL))
                .password(rs.getString(PASSWORD))
                .roles(rolesString)
                .status(UserStatus.valueOf(rs.getString(STATUS)))
                .locale(rs.getString(LOCALE))
                .institution(new Institution(
                        UUID.fromString(rs.getString(INSTITUTION_ID)),
                        rs.getString(INSTITUTION_NAME)
                ))
                .career(new Career(
                        UUID.fromString(rs.getString(CAREER_ID)),
                        rs.getString(CAREER_NAME)
                ))
                .build();
    };

    private static final RowMapper<User> INFO_ROW_MAPPER = (rs, rowNum)  -> {
        Object[] roles = (Object[]) rs.getArray(ROLES).getArray();
        String[] rolesString = Arrays.copyOf(roles, roles.length, String[].class);

        return new User.UserBuilder()
                .userId(UUID.fromString(rs.getString(USER_ID)))
                .firstName(rs.getString(FIRST_NAME))
                .lastName(rs.getString(LAST_NAME))
                .username(rs.getString(USERNAME))
                .email(rs.getString(EMAIL))
                .roles(rolesString)
                .status(UserStatus.valueOf(rs.getString(STATUS)))
                .locale(rs.getString(LOCALE))
                .build();
    };

    private static final RowMapper<ProfilePicture> PROFILE_IMAGE_ROW_MAPPER = (rs, rowNum) -> new ProfilePicture(rs.getString(USER_ID), rs.getObject(IMAGE));


    @Autowired
    public UserJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcRoleInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_ROLES);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    @Override
    public List<User> getStudents(String query, int pageNum, int pageSize) {
        String searchWord = escapeLikeString(query);

        return jdbcTemplate.query("SELECT u.user_id, u.username, u.email, u.first_name, u.last_name, u.locale, u.status, array_agg(r.role_name) as roles FROM users u " +
                "INNER JOIN User_Roles r ON u.user_id = r.user_id " +
                "WHERE NOT EXISTS (SELECT 1 FROM User_Roles ur WHERE ur.user_id = u.user_id AND ur.role_name = 'ROLE_ADMIN') AND (lower(u.username) LIKE lower(?) ESCAPE '!' OR lower(u.email) LIKE lower(?) ESCAPE '!')" +
                "GROUP BY u.user_id ORDER BY u.email LIMIT ? OFFSET ?", INFO_ROW_MAPPER, searchWord, searchWord, pageSize, (pageNum - 1) * pageSize);
    }

    @Override
    public int getStudentsQuantity(String query) {
        String searchWord = escapeLikeString(query);

        List<Object> args = new ArrayList<>();
        args.add(searchWord);
        args.add(searchWord);

        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT u.user_id) " +
                            "FROM users u INNER JOIN User_Roles r ON u.user_id = r.user_id " +
                            "WHERE NOT EXISTS (SELECT 1 FROM User_Roles ur WHERE ur.user_id = u.user_id AND ur.role_name = 'ROLE_ADMIN') " +
                            "AND (lower(u.username) LIKE lower(?) ESCAPE '!' OR lower(u.email) LIKE lower(?) ESCAPE '!')",
                     args.toArray(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public void create(final String email, final String password, final UUID careerId, final String lang, final Role role){
        final MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);
        args.addValue(PASSWORD, password);
        args.addValue(CAREER_ID, careerId);
        args.addValue(LOCALE, lang);

        int rows = namedParameterJdbcTemplate.update("UPDATE users SET password = :password, career_id = :career_id, locale = :locale WHERE email = :email AND password IS NULL",
                args);

        if (rows == 0) {
            namedParameterJdbcTemplate.update("INSERT INTO users (email, password, career_id, locale) VALUES (:email, :password, :career_id, :locale)",
                    args);
        } else {
            LOGGER.warn("Retro-compatible insert: user with email {} updated", email);
        }

        namedParameterJdbcTemplate.update("INSERT INTO User_Roles (user_id, role_name) " +
                "VALUES ((SELECT user_id FROM Users WHERE email = :email), :role_name)",
                new MapSqlParameterSource()
                        .addValue(EMAIL, email)
                        .addValue(ROLE_NAME, role.getRole()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
            return jdbcTemplate.query("SELECT u.*, array_agg(r.role_name) as roles, i.institution_id, i.institution_name, c.career_id, c.career_name FROM users u " +
                    "INNER JOIN User_Roles r ON u.user_id = r.user_id " +
                    "INNER JOIN Careers c ON u.career_id = c.career_id " +
                    "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                    "WHERE u.email = ? " +
                    "GROUP BY u.user_id, c.career_id, i.institution_id", ROW_MAPPER, email).stream().findFirst();
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return jdbcTemplate.query("SELECT u.*, array_agg(r.role_name) as roles, i.institution_id, i.institution_name, c.career_id, c.career_name FROM users u " +
                "INNER JOIN User_Roles r ON u.user_id = r.user_id " +
                "INNER JOIN Careers c ON u.career_id = c.career_id " +
                "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                "WHERE u.user_id = ? " +
                "GROUP BY u.user_id, c.career_id, i.institution_id", ROW_MAPPER, userId).stream().findFirst();
    }
    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT u.*, array_agg(r.role_name) as roles, i.institution_id, i.institution_name, c.career_id, c.career_name FROM users u " +
                "INNER JOIN User_Roles r ON u.user_id = r.user_id " +
                "INNER JOIN Careers c ON u.career_id = c.career_id " +
                "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                "WHERE u.username = ? " +
                "GROUP BY u.user_id, c.career_id, i.institution_id", ROW_MAPPER, username).stream().findFirst();
    }

    @Override
    public boolean update(User user) {
        return jdbcTemplate.update("UPDATE Users SET first_name = ?, last_name = ?, username = ? WHERE user_id = ?",
                user.getFirstName(), user.getLastName(), user.getUsername(), user.getUserId()) == 1;
    }

    @Override
    public Optional<ProfilePicture> getProfilePicture(UUID userId){
        return jdbcTemplate.query("SELECT user_id, image FROM Users u LEFT JOIN Images i ON u.profile_picture_id = i.image_id WHERE user_id = ?",
                    PROFILE_IMAGE_ROW_MAPPER, userId).stream().findFirst();
    }

    @Override
    public UUID updateProfilePicture(UUID userId, byte[] profilePicture) {
        final MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(USER_ID, userId);
        args.addValue(IMAGE, profilePicture);
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update("DELETE FROM Images WHERE image_id = (SELECT profile_picture_id FROM Users WHERE user_id = ?)", userId);
        namedParameterJdbcTemplate.update("INSERT INTO Images (image) VALUES (:image)", args, holder, new String[]{IMAGE_ID});
        int rows = jdbcTemplate.update("UPDATE Users SET profile_picture_id = ? WHERE user_id = ?", holder.getKeys().get(IMAGE_ID), userId);
        if(rows == 0)
            throw new UserNotFoundException();
        return (UUID) holder.getKeys().get(IMAGE_ID);
    }

    @Override
    public void updatePassword(UUID userId, String password) {
        jdbcTemplate.update("UPDATE Users SET password = ? WHERE user_id = ?", password, userId);
    }

    @Override
    public boolean updatePasswordForUserWithEmail(String email, String password) {
        return jdbcTemplate.update("UPDATE Users SET password = ? WHERE email = ?", password, email) == 1;
    }

    @Override
    public int unbanUsers() {
        return jdbcTemplate.update("UPDATE Users u SET status = 'ACTIVE' WHERE status = 'BANNED' AND NOT EXISTS (SELECT * FROM Bans WHERE user_id = u.user_id AND end_date > now())");
    }

    @Override
    public boolean unbanUser(UUID userId) {
        return jdbcTemplate.update("UPDATE Users u SET status = 'ACTIVE' WHERE status = 'BANNED' AND user_id = ?", userId) == 1;
    }

    @Override
    public boolean banUser(UUID userId, UUID adminId, LocalDateTime endDate, String reason) {
        boolean userFound = jdbcTemplate.update("UPDATE Users SET status = 'BANNED' WHERE user_id = ? AND status = 'ACTIVE'", userId) == 1;
        if (!userFound) throw new UserNotFoundException();
        return jdbcTemplate.update("INSERT INTO Bans (user_id, admin_id, end_date, reason) VALUES (?, ?, ?, ?)", userId, adminId, endDate, reason) == 1;
    }
}
