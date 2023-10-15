package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

@Repository
public class DirectoryJdbcDao implements DirectoryDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert jdbcFavoritesInsert;

    private static final RowMapper<Directory> LIMITED_ROW_MAPPER = (rs, rowNum)  -> {
        Directory.DirectoryBuilder dbuilder = new Directory.DirectoryBuilder()
                        .directoryId(UUID.fromString(rs.getString(DIRECTORY_ID)))
                        .name(rs.getString(DIRECTORY_NAME));
        String parentId = rs.getString(PARENT_ID);
        if (parentId != null) dbuilder.parentId(UUID.fromString(parentId));
        return dbuilder.build();
    };

    private static final RowMapper<Directory> ROW_MAPPER = (rs, rowNum) -> {
        String userId = rs.getString(USER_ID);
        String parentId = rs.getString(PARENT_ID);
        Directory.DirectoryBuilder dbuilder =
                new Directory.DirectoryBuilder()
                    .directoryId(UUID.fromString(rs.getString(DIRECTORY_ID)))
                    .name(rs.getString(DIRECTORY_NAME))
                    .createdAt(rs.getTimestamp(CREATED_AT).toLocalDateTime())
                    .lastModifiedAt(rs.getTimestamp(LAST_MODIFIED_AT).toLocalDateTime())
                    .visible(rs.getBoolean(VISIBLE))
                    .iconColor(rs.getString(ICON_COLOR));

        if (userId != null)
            dbuilder.user(new User.UserBuilder()
                    .userId(UUID.fromString(userId))
                    .email(rs.getString(EMAIL))
                    .build()
            );
        if (parentId != null)
            dbuilder.parentId(UUID.fromString(parentId));
        return dbuilder.build();
    };

    private static final RowMapper<Directory> USER_ROW_MAPPER = (rs, rowNum) -> new Directory.DirectoryBuilder()
            .directoryId(UUID.fromString(rs.getString(DIRECTORY_ID)))
            .name(rs.getString(DIRECTORY_NAME))
            .user(new User.UserBuilder()
                    .userId(UUID.fromString(rs.getString(USER_ID)))
                    .email(rs.getString(EMAIL))
                    .locale(rs.getString(LOCALE))
                    .build())
            .iconColor(rs.getString(ICON_COLOR))
            .build();

    private static final RowMapper<Directory> ROW_MAPPER_WITH_SUBJECT = (rs, rowNum)  -> {
        Directory.DirectoryBuilder dbuilder =
                new Directory.DirectoryBuilder()
                    .directoryId(UUID.fromString(rs.getString(DIRECTORY_ID)))
                    .name(rs.getString(DIRECTORY_NAME));

        String subjectId = rs.getString(SUBJECT_ID);
        if (subjectId != null)
            dbuilder.subject(new Subject(UUID.fromString(subjectId), rs.getString(SUBJECT_NAME)));
        else
            dbuilder.parentId(UUID.fromString(rs.getString(PARENT_ID)));
        return dbuilder.build();
    };

    @Autowired
    public DirectoryJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcFavoritesInsert = new SimpleJdbcInsert(ds)
                .withTableName(FAVORITES);
    }

    @Override
    public UUID create(String name, UUID parentId, UUID userId, boolean visible, String iconColor) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(DIRECTORY_NAME, name);
        args.addValue(PARENT_ID, parentId);
        args.addValue(USER_ID, userId);
        args.addValue(VISIBLE, visible);
        args.addValue(ICON_COLOR, iconColor);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Directories (directory_name, user_id, parent_id, visible, icon_color) " +
                        "SELECT :directory_name, :user_id, d.directory_id, :visible, :icon_color FROM Directories d " +
                        "WHERE d.directory_id = :parent_id AND (d.user_id = :user_id OR d.parent_id IS NULL)",
                args, holder, new String[]{DIRECTORY_ID});
        return (UUID) holder.getKeys().get(DIRECTORY_ID);
    }

    @Override
    public UUID createRootDirectory(String name) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(DIRECTORY_NAME, name);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Directories (directory_name, visible) " +
                        "VALUES (:directory_name, true)",
                args, holder, new String[]{DIRECTORY_ID});
        return (UUID) holder.getKeys().get(DIRECTORY_ID);
    }

    @Override
    public Optional<Directory> getDirectoryById(UUID directoryId, UUID currentUserId) {
        MapSqlParameterSource args = new MapSqlParameterSource(DIRECTORY_ID, directoryId);
        return namedParameterJdbcTemplate.query("SELECT d.*, u.user_id, u.email FROM Directories d LEFT JOIN Users u ON d.user_id = u.user_id WHERE directory_id = :directory_id AND (visible " + getVisibilityCondition(currentUserId, args) + ")",
                args, ROW_MAPPER).stream().findFirst();
    }

    private String getVisibilityCondition(UUID currentUserId, MapSqlParameterSource args) {
        if (currentUserId != null) args.addValue(USER_ID, currentUserId);
        return currentUserId != null? "OR d.user_id = :user_id" : "";
    }

    @Override
    public DirectoryPath getDirectoryPath(UUID directoryId) {
        // Right now no user validation is needed, might change later
        List<Directory> dirs = jdbcTemplate.query("WITH RECURSIVE Ancestors(directory_id, directory_name, parent_id, level) AS ( " +
                        "SELECT d.directory_id, d.directory_name, d.parent_id, 0 as level FROM Directories d WHERE d.directory_id = ? " +
                        "UNION " +
                        "SELECT d.directory_id, d.directory_name, d.parent_id, a.level + 1 FROM Ancestors a INNER JOIN Directories d ON a.parent_id = d.directory_id " +
                        ") SELECT * FROM Ancestors LEFT JOIN Subjects s ON s.root_directory_id = directory_id " +
                        "ORDER BY level DESC ",
                ROW_MAPPER_WITH_SUBJECT, directoryId);

        return new DirectoryPath(dirs);
    }

    @Override
    public boolean update(Directory directory, UUID currentUserId) {
        return jdbcTemplate.update("UPDATE Directories SET directory_name = ?, icon_color = ?, visible = ? WHERE directory_id = ? AND user_id = ?",
                directory.getName(), directory.getIconColor(), directory.isVisible(), directory.getId(), currentUserId) == 1;
    }


    @Override
    public boolean delete(UUID[] directoryIds, UUID currentUserId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(DIRECTORY_ID, Arrays.asList(directoryIds));
        args.addValue(USER_ID, currentUserId);
        return namedParameterJdbcTemplate.update("DELETE FROM Directories WHERE directory_id IN (:directory_id) AND user_id = :user_id", args) >= directoryIds.length;
    }

    @Override
    public List<Directory> delete(UUID[] directoryIds) {
        MapSqlParameterSource args = new MapSqlParameterSource(DIRECTORY_ID, Arrays.asList(directoryIds));
        List<Directory> dir = new ArrayList<>(
                namedParameterJdbcTemplate.query("SELECT d.directory_id, d.directory_name, d.icon_color, " +
                                "u.user_id, u.email, u.locale FROM Directories d INNER JOIN Users u ON d.user_id = u.user_id WHERE directory_id IN (:directory_id)",
                args, USER_ROW_MAPPER)
        );
        int rowsDeleted = namedParameterJdbcTemplate.update("DELETE FROM Directories WHERE directory_id IN (:directory_id)", args);
        if (rowsDeleted != dir.size()) return Collections.emptyList(); // TODO: Check if this makes sense
        return dir;
    }

    @Override
    public boolean deleteRootDirectory(UUID directoryId) {
        return jdbcTemplate.update("DELETE FROM Directories d WHERE d.directory_id = ? AND d.parent_id IS NULL AND d.user_id IS NULL", directoryId) == 1;
    }

    @Override
    public List<Directory> getFavorites(UUID userId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(USER_ID, userId);
        return namedParameterJdbcTemplate.query("SELECT d.*, u.user_id, u.email FROM Favorites f " +
                "INNER JOIN Directories d ON d.directory_id = f.directory_id " +
                "INNER JOIN Users u ON u.user_id = d.user_id " +
                "WHERE f.user_id = :user_id AND (d.visible OR d.user_id = :user_id)", args, ROW_MAPPER);
    }

    @Override
    public void addFavorite(UUID userId, UUID directoryId) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(USER_ID, userId);
        args.put(DIRECTORY_ID, directoryId);
        jdbcFavoritesInsert.execute(args);
    }

    @Override
    public boolean removeFavorite(UUID userId, UUID directoryId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(USER_ID, userId);
        args.addValue(DIRECTORY_ID, directoryId);
        return namedParameterJdbcTemplate.update("DELETE FROM Favorites WHERE user_id = :user_id AND directory_id = :directory_id", args) == 1;
    }
}