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
                rs.getString(NAME),
                rs.getString(PARENT_ID) != null?  UUID.fromString(rs.getString(PARENT_ID)) : null
        );

    private static final RowMapper<Directory> ROW_MAPPER_WITH_SUBJECT = (rs, rowNum)  -> {
        String subjectId = rs.getString(SUBJECT_ID);
        if (subjectId != null) {
            return new RootDirectory(
                    UUID.fromString(rs.getString(DIRECTORY_ID)),
                    rs.getString(NAME),
                    null,
                    new Subject(
                            UUID.fromString(subjectId),
                            rs.getString(NAME)
                    ));
        } else {
            return new Directory(
                    UUID.fromString(rs.getString(DIRECTORY_ID)),
                    rs.getString(NAME),
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

    @Override
    public Directory create(String name, UUID parentId, UUID userId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NAME, name);
        args.addValue(PARENT_ID, parentId);
        args.addValue(USER_ID, userId);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Directories (name, user_id, parent_id) VALUES (:name, :user_id, :parent_id)",
                args, holder, new String[]{DIRECTORY_ID});
        UUID directoryId = (UUID) holder.getKeys().get(DIRECTORY_ID);
        return new Directory(directoryId, name, parentId, userId);
    }

    @Override
    public List<Directory> search(SearchArguments sa) {
        // TODO: add created_at column to directory
        StringBuilder query = new StringBuilder(
                "SELECT d.name, d.directory_id, d.parent_id FROM Directories d " +
                        "INNER JOIN Subjects s ON d.directory_id = s.root_directory_id " +
                        "INNER JOIN Careers c ON s.career_id = c.career_id " +
                        "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                        "WHERE true " // TODO: Ask if this is legal
        );

        // TODO: Modularize?
        List<Object> args = new ArrayList<>();
        addIfPresent(query, args, "i."  + INSTITUTION_ID, "=", "AND", sa.getInstitutionId());
        addIfPresent(query, args, "c." + CAREER_ID, "=","AND", sa.getCareerId());
        addIfPresent(query, args, "s." + SUBJECT_ID, "=","AND", sa.getSubjectId());

        sa.getWord().ifPresent(w -> {
                    String searchWord = "%" + w + "%";
                    query.append("AND LOWER(d.name) LIKE LOWER(?) OR LOWER(i.name) LIKE LOWER(?) OR LOWER(c.name) LIKE LOWER(?) OR LOWER(s.name) LIKE LOWER(?)");
                    for (int i = 0; i < 4; i++)
                        args.add(searchWord);
                }
        );

        query.append(" ORDER BY ").append(JdbcDaoUtils.SORTBY.get(sa.getSortBy()));
        if (!sa.isAscending()) query.append(" DESC");
        query.append(" LIMIT ").append(sa.getPageSize()).append(" OFFSET ").append((sa.getPage() - 1) * sa.getPageSize());
        return jdbcTemplate.query(query.toString(), args.toArray(), ROW_MAPPER);
    }

    @Override
    public Directory getDirectoryById(UUID directoryId) {
        return jdbcTemplate.queryForObject("SELECT * FROM Directories WHERE directory_id = ?", ROW_MAPPER, directoryId);
    }

    @Override
    public List<Directory> getChildren(UUID directoryId) {
        return jdbcTemplate.query("SELECT * FROM Directories WHERE parent_id = ?", ROW_MAPPER, directoryId);
    }


    @Override
    public DirectoryPath getDirectoryPath(UUID directoryId) {
        List<Directory> dirs = jdbcTemplate.query("WITH RECURSIVE Ancestors(directory_id, name, parent_id, level) AS ( " +
                        "VALUES(null, null, ?, 0) " +
                        "UNION " +
                        "SELECT d.directory_id, d.name, d.parent_id, a.level + 1 FROM Ancestors a INNER JOIN Directories d ON a.parent_id = d.directory_id " +
                        ") SELECT * FROM Ancestors LEFT JOIN Subjects s ON s.root_directory_id = directory_id " +
                        "WHERE directory_id IS NOT NULL ORDER BY level DESC ",
                ROW_MAPPER_WITH_SUBJECT, directoryId);

        return new DirectoryPath(dirs);
    }

    @Override
    public void delete(UUID directoryId) {
        jdbcTemplate.update("DELETE FROM Directories WHERE directory_id = ?", directoryId);
    }

}