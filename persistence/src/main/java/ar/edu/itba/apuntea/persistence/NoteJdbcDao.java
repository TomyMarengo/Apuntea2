package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Category;
import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.models.SearchArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import static ar.edu.itba.apuntea.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class NoteJdbcDao implements NoteDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Note> ROW_MAPPER = (rs, rowNum) ->
            new Note(
                    UUID.fromString(rs.getString(NOTE_ID)),
                    rs.getString(NAME),
                    Category.valueOf(rs.getString(CATEGORY).toUpperCase()),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                    rs.getFloat(AVG_SCORE)
            );

    @Autowired
    public NoteJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Notes")
                .usingGeneratedKeyColumns(NOTE_ID)
                .usingColumns(NAME, FILE, SUBJECT_ID, CATEGORY); // TODO: Move to resource/constants?
    }

    @Override
    public Note create(MultipartFile multipartFile, String name) {
        byte[] bytes = new byte[0];
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        final Map<String, Object> args = new HashMap<>();
        args.put(NAME, name);
        args.put(FILE, bytes);
        args.put(SUBJECT_ID, UUID.fromString("323e4567-e89b-12d3-a456-426655440000")); // TODO: Remove
        args.put(CATEGORY, "practice"); // TODO change
        UUID noteId = (UUID) jdbcInsert.executeAndReturnKeyHolder(args).getKeys().get("note_id");
        return new Note(noteId, name);
    }

    @Override
    public byte[] getNoteFileById(UUID noteId){
        final byte[] file = jdbcTemplate.queryForObject("SELECT file FROM Notes WHERE note_id = ?",
                new Object[]{noteId}, (rs, rowNum) -> (byte[]) rs.getObject(FILE));
        // TODO: Move mapper to a constant?
        return file;
    }

    // automate consumer for add to query
    private static void addIfPresent(StringBuilder query, List<Object> args, String field, String cmpOp, Optional<?> value) {
        value.ifPresent(val -> {
            query.append("AND ").append(field).append(" ").append(cmpOp).append(" ? ");
            args.add(val);
        });
    }

    @Override
    public List<Note> search(SearchArguments sa) {
        StringBuilder query = new StringBuilder(
                "SELECT n.name, n.note_id, n.created_at, n.category, AVG(r.score) AS avg_score FROM Notes n " +
                        "INNER JOIN Subjects s ON n.subject_id = s.subject_id " +
                        "INNER JOIN Careers c ON s.career_id = c.career_id " +
                        "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                        "LEFT JOIN Reviews r ON n.note_id = r.note_id " +
                        "WHERE true " // TODO: Ask if this is legal
        );
        List<Object> args = new ArrayList<>();

        addIfPresent(query, args, "i."  + INSTITUTION_ID, "=", sa.getInstitution());
        addIfPresent(query, args, "c." + CAREER_ID, "=", sa.getCareer());
        addIfPresent(query, args, "s." + SUBJECT_ID, "=", sa.getSubject());
        addIfPresent(query, args, CATEGORY, "=", sa.getCategory().map(Enum::toString));

        query.append("GROUP BY n.").append(NOTE_ID);
        sa.getScore().ifPresent( score -> query.append(" HAVING AVG(r.score) >= ").append(score));

        query.append(" ORDER BY ").append(JdbcDaoUtils.SORTBY.get(sa.getSortBy()));
        if (!sa.isAscending()) query.append(" DESC");
        query.append(" LIMIT ").append(sa.getPageSize()).append(" OFFSET ").append((sa.getPage() - 1) * sa.getPageSize());

        return jdbcTemplate.query(query.toString(), args.toArray(), ROW_MAPPER);
    }
}
