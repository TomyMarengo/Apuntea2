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
                    UUID.fromString(rs.getString("note_id")),
                    rs.getString("name")
            );

    @Autowired
    public NoteJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Notes")
                .usingGeneratedKeyColumns("note_id");
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

}
