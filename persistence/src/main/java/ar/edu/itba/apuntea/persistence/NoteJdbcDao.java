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
                    UUID.fromString(rs.getString("note_id")),
                    rs.getString("name"),
                    Category.valueOf(rs.getString("category").toUpperCase()),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );

    @Autowired
    public NoteJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Notes")
                .usingGeneratedKeyColumns("note_id")
                .usingColumns("name", "file", "subject_id", "category"); // TODO: Move to resource/constants?
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
        args.put("name", name);
        args.put("file", bytes);
        args.put("subject_id", UUID.fromString("323e4567-e89b-12d3-a456-426655440000")); // TODO: Remove
        args.put("category", "practice");
        UUID noteId = (UUID) jdbcInsert.executeAndReturnKeyHolder(args).getKeys().get("note_id");
        return new Note(noteId, name);
    }

    @Override
    public byte[] getNoteFileById(UUID noteId){
        final byte[] file = jdbcTemplate.queryForObject("SELECT file FROM Notes WHERE note_id = ?",
                new Object[]{noteId}, (rs, rowNum) -> (byte[]) rs.getObject("file"));
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
                "SELECT * FROM Notes n " +
                        "INNER JOIN Subjects s ON n.subject_id = s.subject_id " +
                        "INNER JOIN Careers c ON s.career_id = c.career_id " +
                        "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                        "WHERE true " // TODO: Ask if this is legal
        );
        List<Object> args = new ArrayList<>();

        addIfPresent(query, args, "i.institution_id", "=", sa.getInstitution());
        addIfPresent(query, args, "c.career_id", "=", sa.getCareer());
        addIfPresent(query, args, "s.subject_id", "=", sa.getSubject());
        addIfPresent(query, args, "category", "=", sa.getCategory());
        addIfPresent(query, args, "score", ">=", sa.getScore());

        return jdbcTemplate.query(query.toString(), args.toArray(), ROW_MAPPER);
    }
}
