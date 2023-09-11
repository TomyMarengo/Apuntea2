package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.SearchArguments;
//import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.IntStream;

@Repository
public class NoteJdbcDao implements NoteDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
//    private final Tika tika = new Tika();

    private final static RowMapper<Note> ROW_MAPPER = (rs, rowNum) ->
            new Note(
                    UUID.fromString(rs.getString(NOTE_ID)),
                    rs.getString(NAME),
                    Category.valueOf(rs.getString(CATEGORY).toUpperCase()),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                    rs.getFloat(AVG_SCORE)
            );
    private final static RowMapper<Note> ROW_MAPPER_WITH_FILE = (rs, rowNum) ->
            new Note(
                    UUID.fromString(rs.getString(NOTE_ID)),
                    rs.getString(NAME),
                    Category.valueOf(rs.getString(CATEGORY).toUpperCase()),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                    rs.getFloat(AVG_SCORE),
                    rs.getBytes(FILE)
            );

    @Autowired
    public NoteJdbcDao(final DataSource ds, final UserDao userDao){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Notes")
                .usingGeneratedKeyColumns(NOTE_ID)
                .usingColumns(NAME, FILE, SUBJECT_ID, CATEGORY, USER_ID); // TODO: Move to resource/constants
    }

    @Override
    public Note create(MultipartFile file, String name, UUID user_id, UUID subjectId, String category) {
        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
//        String contentType = tika.detect(file.getOriginalFilename());
//        if (!contentType.equals("application/pdf")) {
//            throw new IllegalArgumentException("File must be a PDF");
//        }

        final Map<String, Object> args = new HashMap<>();
        args.put(NAME, name);
        args.put(FILE, bytes);
        args.put(SUBJECT_ID, subjectId);
        args.put(CATEGORY, category.toLowerCase());

        args.put(USER_ID, user_id);

        UUID noteId = (UUID) jdbcInsert.executeAndReturnKeyHolder(args).getKeys().get(NOTE_ID);
        return new Note(noteId, name);
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT n.name, n.file, n.note_id, n.created_at, n.category, AVG(r.score) AS avg_score FROM Notes n LEFT JOIN Reviews r ON n.note_id = r.note_id WHERE n.note_id = ? GROUP BY n.note_id",
                    ROW_MAPPER_WITH_FILE,
                    noteId
                )
        );
    }

    @Override
    public byte[] getNoteFileById(UUID noteId){
        final byte[] file = jdbcTemplate.queryForObject("SELECT file FROM Notes WHERE note_id = ?",
                new Object[]{noteId}, (rs, rowNum) -> (byte[]) rs.getObject(FILE));
        // TODO: Move mapper to a constant?
        return file;
    }


    @Override
    public List<Note> getNotesByParentDirectoryId(UUID directory_id) {
        return jdbcTemplate.query("SELECT n.name, n.note_id, n.created_at, n.category, AVG(r.score) AS avg_score FROM Notes n " +
                "LEFT JOIN Reviews r ON n.note_id = r.note_id " +
                "WHERE parent_directory_id = ? " +
                "GROUP BY n.note_id",
                ROW_MAPPER, directory_id);
    }

    @Override
    public List<Note> search(SearchArguments sa) {
        StringBuilder query = new StringBuilder(
                "SELECT n.note_id, n.name, n.category, n.created_at, AVG(r.score) AS avg_score FROM Notes n " +
                        "INNER JOIN Subjects s ON n.subject_id = s.subject_id " +
                        "INNER JOIN Careers c ON s.career_id = c.career_id " +
                        "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                        "LEFT JOIN Reviews r ON n.note_id = r.note_id " +
                        "WHERE true "
        );
        List<Object> args = new ArrayList<>();

        addIfPresent(query, args, "i."  + INSTITUTION_ID, "=", "AND", sa.getInstitutionId());
        addIfPresent(query, args, "c." + CAREER_ID, "=", "AND", sa.getCareerId());
        addIfPresent(query, args, "s." + SUBJECT_ID, "=", "AND", sa.getSubjectId());
        addIfPresent(query, args, CATEGORY, "=", "AND", sa.getCategory().map(Enum::toString).map(String::toLowerCase));

        sa.getWord().ifPresent(w -> {
                String searchWord = "%" + w + "%";
                query.append("AND LOWER(n.name) LIKE LOWER(?) OR LOWER(i.name) LIKE LOWER(?) OR LOWER(c.name) LIKE LOWER(?) OR LOWER(s.name) LIKE LOWER(?)");
                for (int i = 0; i < 4; i++)
                    args.add(searchWord);
            }
        );

        query.append("GROUP BY n.").append(NOTE_ID);
        sa.getScore().ifPresent( score -> query.append(" HAVING AVG(r.score) >= ").append(score));

        if (sa.getSortBy() != null) {
            query.append(" ORDER BY ").append(JdbcDaoUtils.SORTBY.get(sa.getSortBy()));
            if (!sa.isAscending()) query.append(" DESC");
        }

        query.append(" LIMIT ").append(sa.getPageSize()).append(" OFFSET ").append((sa.getPage() - 1) * sa.getPageSize());

        return jdbcTemplate.query(query.toString(), args.toArray(), ROW_MAPPER);
    }

    @Override
    public Integer createOrUpdateReview(UUID noteId, UUID userId, Integer score) {
        jdbcTemplate.update("MERGE INTO reviews r USING (VALUES(?, ?, ?)) as t(u,n,s) ON r.user_id = t.u AND r.note_id = t.n " +
                "WHEN MATCHED THEN UPDATE SET score = t.s " +
                "WHEN NOT MATCHED THEN INSERT (user_id, note_id, score) VALUES(t.u, t.n, t.s)", userId, noteId, score);
        return score;
    }
}
