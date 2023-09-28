package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.*;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

@Repository
public class DirectoryJdbcDao implements DirectoryDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Directory> ROW_MAPPER = (rs, rowNum)  ->
        new Directory(
                UUID.fromString(rs.getString(DIRECTORY_ID)),
                rs.getString(DIRECTORY_NAME),
                rs.getString(PARENT_ID) != null?  UUID.fromString(rs.getString(PARENT_ID)) : null
        );

    private static final RowMapper<Directory> ROW_MAPPER_WITH_SUBJECT = (rs, rowNum)  -> {
        String subjectId = rs.getString(SUBJECT_ID);
        if (subjectId != null) {
            return new RootDirectory(
                    UUID.fromString(rs.getString(DIRECTORY_ID)),
                    rs.getString(DIRECTORY_NAME),
                    null,
                    new Subject(
                            UUID.fromString(subjectId),
                            rs.getString(SUBJECT_NAME)
                    ));
        } else {
            return new Directory(
                    UUID.fromString(rs.getString(DIRECTORY_ID)),
                    rs.getString(DIRECTORY_NAME),
                    UUID.fromString(rs.getString(PARENT_ID))
            );
        }
    };

    @Autowired
    public DirectoryJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Directories")
                .usingGeneratedKeyColumns(DIRECTORY_ID);
    }

    @Transactional
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
    public Directory getDirectoryById(UUID directoryId, UUID currentUserId) {
        return jdbcTemplate.queryForObject("SELECT * FROM Directories WHERE directory_id = ? AND (visible OR user_id = ?)", ROW_MAPPER, directoryId, currentUserId);
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
    public boolean deleteMany(UUID[] directoryIds, UUID currentUserId) {
        StringBuilder directoryIdsBuilder = new StringBuilder();
        Arrays.stream(directoryIds).forEach(directoryId -> directoryIdsBuilder.append(", '").append(directoryId.toString()).append("'"));
        directoryIdsBuilder.replace(0, 2, "");
        return jdbcTemplate.update("DELETE FROM Directories WHERE directory_id IN ("+directoryIdsBuilder.toString()+") AND user_id = ?", currentUserId) >= directoryIds.length;
    }

    @Override
    public boolean delete(UUID directoryId, UUID currentUserId) {
        return jdbcTemplate.update("DELETE FROM Directories WHERE directory_id = ? AND user_id = ?", directoryId, currentUserId) == 1;
    }

}