package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.SearchArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

@Repository
public class DirectoryJdbcDao implements DirectoryDao {
    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Directory> ROW_MAPPER = (rs, rowNum)  ->
        new Directory(
                UUID.fromString(rs.getString(DIRECTORY_ID)),
                rs.getString(NAME),
                rs.getString(PARENT_ID) != null?  UUID.fromString(rs.getString(PARENT_ID)) : null
        );

    @Autowired
    public DirectoryJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Directories")
                .usingGeneratedKeyColumns(DIRECTORY_ID);
    }

    @Override
    public Directory create(String name, UUID parentId, UUID userId) {
        Map<String, Object> args = new HashMap<>();
        args.put(NAME, name);
        args.put(PARENT_ID, parentId);
        args.put(USER_ID, userId);
        UUID directoryId = (UUID) jdbcInsert.executeAndReturnKeyHolder(args).getKeys().get(DIRECTORY_ID);
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

        query.append(" ORDER BY ").append(JdbcDaoUtils.SORTBY.get(sa.getSortBy()));
        if (!sa.isAscending()) query.append(" DESC");
        query.append(" LIMIT ").append(sa.getPageSize()).append(" OFFSET ").append((sa.getPage() - 1) * sa.getPageSize());
        return jdbcTemplate.query(query.toString(), args.toArray(), ROW_MAPPER);
    }

    @Override
    public Directory getDirectoryById(UUID directory_id) {
        return jdbcTemplate.queryForObject("SELECT * FROM Directories WHERE directory_id = ?", ROW_MAPPER, directory_id);
    }

    @Override
    public List<Directory> getChildren(UUID directory_id) {
        return jdbcTemplate.query("SELECT * FROM Directories WHERE parent_id = ?", ROW_MAPPER, directory_id);
    }


}
