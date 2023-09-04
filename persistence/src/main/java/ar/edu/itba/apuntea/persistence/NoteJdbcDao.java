package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class NoteJdbcDao implements NoteDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Note> ROW_MAPPER = (rs, rowNum) ->
            new Note(
                    UUID.fromString(rs.getString("noteId")),
                    rs.getString("institution"),
                    rs.getString("career"),
                    rs.getString("subject"),
                    rs.getString("type"),
                    rs.getBytes("file")
            );

    @Autowired
    public NoteJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);

        // TODO: move to schema.sql!!!
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS note (" +
                "noteId UUID PRIMARY KEY DEFAULT gen_random_uuid ()," +
                "institution VARCHAR(100) NOT NULL," +
                "career VARCHAR(100) NOT NULL," +
                "subject VARCHAR(100) NOT NULL," +
                "type VARCHAR(100) NOT NULL," +
                "file BYTEA NOT NULL" +
                ");");

        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("note")
                .usingGeneratedKeyColumns("noteid");
    }

    @Override
    public Note create(MultipartFile multipartFile, String institution, String career, String subject, String type) {
        byte[] bytes = new byte[0];
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("institution", institution);
        args.put("career", career);
        args.put("subject", subject);
        args.put("type", type);
        args.put("file", bytes);
        KeyHolder holder = jdbcInsert.executeAndReturnKeyHolder(args);
        UUID noteId = (UUID) holder.getKeys().get("noteId");


        return new Note(noteId, institution, career, subject, type, bytes);
    }

    public byte[] getNoteFileById(UUID noteId){
        final byte[] file = jdbcTemplate.queryForObject("SELECT file FROM note WHERE noteid = ?",
                new Object[]{noteId}, (rs, rowNum) -> (byte[]) rs.getObject("file"));
        // TODO: Move mapper to a constant?
        return file;
    }

}
