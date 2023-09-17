package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
//import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class NoteJdbcDao implements NoteDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcNoteInsert;
    private final SimpleJdbcInsert jdbcReviewInsert;
    //    private final Tika tika = new Tika();

    private final static RowMapper<Note> ROW_MAPPER = (rs, rowNum) ->
            new Note(
                    UUID.fromString(rs.getString(NOTE_ID)),
                    rs.getString(NOTE_NAME),
                    Category.valueOf(rs.getString(CATEGORY).toUpperCase()),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                    rs.getFloat(AVG_SCORE),
                    new Subject(
                            UUID.fromString(rs.getString(SUBJECT_ID)),
                            rs.getString(SUBJECT_NAME),
                            UUID.fromString(rs.getString(ROOT_DIRECTORY_ID))
                    )
            );
    private final static RowMapper<Note> ROW_MAPPER_WITH_FILE = (rs, rowNum) ->
            new Note(
                    UUID.fromString(rs.getString(NOTE_ID)),
                    rs.getString(NOTE_NAME),
                    Category.valueOf(rs.getString(CATEGORY).toUpperCase()),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                    rs.getFloat(AVG_SCORE),
                    new Subject(
                            UUID.fromString(rs.getString(SUBJECT_ID)),
                            rs.getString(SUBJECT_NAME),
                            UUID.fromString(rs.getString(ROOT_DIRECTORY_ID))
                    ),
                    rs.getBytes(FILE)
            );

    private final static RowMapper<Review> REVIEW_ROW_MAPPER = (rs, rowNum) ->
            new Review(
                    new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL)
                    ),
                    rs.getString(CONTENT),
                    rs.getInt(SCORE)
            );

    @Autowired
    public NoteJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcNoteInsert = new SimpleJdbcInsert(ds)
                .withTableName(NOTES)
                .usingGeneratedKeyColumns(NOTE_ID)
                .usingColumns(NOTE_NAME, FILE, SUBJECT_ID, CATEGORY, USER_ID);
        this.jdbcReviewInsert = new SimpleJdbcInsert(ds)
                .withTableName(REVIEWS)
                .usingColumns(NOTE_ID, USER_ID, SCORE);
    }

    @Override
    public Note create(byte[] file, String name, UUID user_id, UUID subjectId, String category) {
//        String contentType = tika.detect(file.getOriginalFilename());
//        if (!contentType.equals("application/pdf")) {
//            throw new IllegalArgumentException("File must be a PDF");
//        }

        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_NAME, name);
        args.addValue(FILE, file);
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(CATEGORY, category.toLowerCase());
        args.addValue(USER_ID, user_id);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Notes (note_name, file, subject_id, category, user_id, parent_id) " +
                " SELECT :note_name, :file, :subject_id, :category, :user_id, s.root_directory_id FROM Subjects s WHERE s.subject_id = :subject_id"
                , args, holder, new String[]{NOTE_ID});
        UUID noteId = (UUID) holder.getKeys().get(NOTE_ID);
        return new Note(noteId, name);
    }

    @Override
    public Note create(byte[] file, String name, UUID user_id, UUID subjectId, String category, UUID parentId) {
//        String contentType = tika.detect(file.getOriginalFilename());
//        if (!contentType.equals("application/pdf")) {
//            throw new IllegalArgumentException("File must be a PDF");
//        }

        final Map<String, Object> args = new HashMap<>();
        args.put(NOTE_NAME, name);
        args.put(FILE, file);
        args.put(SUBJECT_ID, subjectId);
        args.put(CATEGORY, category.toLowerCase());
        args.put(PARENT_ID, parentId);

        args.put(USER_ID, user_id);

        UUID noteId = (UUID) jdbcNoteInsert.executeAndReturnKeyHolder(args).getKeys().get(NOTE_ID);
        return new Note(noteId, name);
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT n.note_name, n.file, n.note_id, n.created_at, n.category, AVG(r.score) AS avg_score , s.subject_id, s.subject_name, s.root_directory_id " +
                                "FROM Notes n LEFT JOIN Reviews r ON n.note_id = r.note_id JOIN Subjects s ON n.subject_id = s.subject_id WHERE n.note_id = ? GROUP BY n.note_id, s.subject_id",
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
        return jdbcTemplate.query("SELECT n.note_id, n.note_name, n.created_at, n.category, AVG(r.score) AS avg_score, s.subject_id, s.subject_name, s.root_directory_id FROM Notes n " +
                "INNER JOIN Subjects s ON n.subject_id = s.subject_id " +
                "LEFT JOIN Reviews r ON n.note_id = r.note_id " +
                "WHERE parent_id = ? " +
                "GROUP BY n.note_id, s.subject_id",
                ROW_MAPPER, directory_id);
    }

    @Override
    public List<Note> search(SearchArguments sa) {
        StringBuilder query = new StringBuilder(
                "SELECT DISTINCT n.note_id, n.note_name, n.category, n.created_at, AVG(r.score) AS avg_score, s.subject_id, s.subject_name, s.root_directory_id FROM Notes n " +
                        "INNER JOIN Subjects s ON n.subject_id = s.subject_id " +
                        "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id " +
                        "INNER JOIN Careers c ON sc.career_id = c.career_id " +
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
                query.append("AND LOWER(n.note_name) LIKE LOWER(?) OR LOWER(i.institution_name) LIKE LOWER(?) OR LOWER(c.career_name) LIKE LOWER(?) OR LOWER(s.subject_name) LIKE LOWER(?)");
                for (int i = 0; i < 4; i++)
                    args.add(searchWord);
            }
        );

        query.append("GROUP BY n.").append(NOTE_ID).append(", s.").append(SUBJECT_ID);
        sa.getScore().ifPresent( score -> query.append(" HAVING AVG(r.score) >= ").append(score));

        if (sa.getSortBy() != null) {
            query.append(" ORDER BY ").append(JdbcDaoUtils.SORTBY.getOrDefault(sa.getSortBy(), NOTE_NAME));
            if (!sa.isAscending()) query.append(" DESC");
        }

        query.append(" LIMIT ").append(sa.getPageSize()).append(" OFFSET ").append((sa.getPage() - 1) * sa.getPageSize());

        return jdbcTemplate.query(query.toString(), args.toArray(), ROW_MAPPER);
    }

    @Override
    public Integer createOrUpdateReview(UUID noteId, UUID userId, Integer score) {
        try {
            jdbcReviewInsert.execute(new HashMap<String, Object>(){{
                put(NOTE_ID, noteId);
                put(USER_ID, userId);
                put(SCORE, score);
            }});
        } catch (DuplicateKeyException e) {
            jdbcTemplate.update("UPDATE Reviews SET score = ? WHERE note_id = ? AND user_id = ?", score, noteId, userId);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
        return score;
    }

    @Override
    public void delete(UUID noteId) {
        jdbcTemplate.update("DELETE FROM Notes WHERE note_id = ?", noteId);
    }

    @Override
    public List<Review> getReviews(UUID noteId) {
        return jdbcTemplate.query(
                "SELECT u.user_id, u.email, r.score, r.content FROM Reviews r INNER JOIN Users u ON r.user_id = u.user_id WHERE r.note_id = ?",
                    new Object[]{noteId},
                    REVIEW_ROW_MAPPER
        );
    }
}
