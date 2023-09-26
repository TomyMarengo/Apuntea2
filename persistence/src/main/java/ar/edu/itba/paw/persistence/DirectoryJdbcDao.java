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
    public UUID create(String name, UUID parentId, UUID userId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(DIRECTORY_NAME, name);
        args.addValue(PARENT_ID, parentId);
        args.addValue(USER_ID, userId);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Directories (directory_name, user_id, parent_id) VALUES (:directory_name, :user_id, :parent_id)",
                args, holder, new String[]{DIRECTORY_ID});
        return (UUID) holder.getKeys().get(DIRECTORY_ID);
    }

    @Override
    public Directory getDirectoryById(UUID directoryId) {
        return jdbcTemplate.queryForObject("SELECT * FROM Directories WHERE directory_id = ?", ROW_MAPPER, directoryId);
    }

    @Override
    public DirectoryPath getDirectoryPath(UUID directoryId) {
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
    public void delete(UUID directoryId) {
        jdbcTemplate.update("DELETE FROM Directories WHERE directory_id = ?", directoryId);
    }

}