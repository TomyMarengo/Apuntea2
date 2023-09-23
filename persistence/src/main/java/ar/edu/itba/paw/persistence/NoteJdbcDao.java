package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
//import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.util.*;


@Repository
public class NoteJdbcDao implements NoteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(NoteJdbcDao.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcNoteInsert;
    private final SimpleJdbcInsert jdbcReviewInsert;

    private final static RowMapper<Note> ROW_MAPPER = (rs, rowNum) ->
            new Note(
                    UUID.fromString(rs.getString(NOTE_ID)),
                    rs.getString(NOTE_NAME),
                    new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL)
                    ),
                    UUID.fromString(rs.getString(PARENT_ID)),
                    new Subject(
                            UUID.fromString(rs.getString(SUBJECT_ID)),
                            rs.getString(SUBJECT_NAME),
                            UUID.fromString(rs.getString(ROOT_DIRECTORY_ID))
                    ),
                    Category.valueOf(rs.getString(CATEGORY)),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                    rs.getTimestamp(LAST_MODIFIED_AT).toLocalDateTime(),
                    rs.getString(FILE_TYPE),
                    rs.getFloat(AVG_SCORE)
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
    private final static RowMapper<Review> COMPLETE_REVIEW_ROW_MAPPER = (rs, rowNum) ->
            new Review(
                    new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL)
                    ),
                    rs.getString(CONTENT),
                    rs.getInt(SCORE),
                    new Note(
                            UUID.fromString(rs.getString(NOTE_ID)),
                            rs.getString(NOTE_NAME),
                            new User(
                                    UUID.fromString(rs.getString(OWNER_ID)),
                                    rs.getString(OWNER_EMAIL)
                            )
                    )
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
                .usingColumns(NOTE_ID, USER_ID, SCORE, CONTENT);
    }

    @Override
    @Transactional
    public UUID create(byte[] file, String name, UUID user_id, UUID subjectId, String category, String file_type) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_NAME, name);
        args.addValue(FILE, file);
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(CATEGORY, category.toLowerCase());
        args.addValue(USER_ID, user_id);
        args.addValue(FILE_TYPE, file_type);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Notes (note_name, file, subject_id, category, user_id, parent_id, file_type) " +
                " SELECT :note_name, :file, :subject_id, :category, :user_id, s.root_directory_id, :file_type FROM Subjects s WHERE s.subject_id = :subject_id"
                , args, holder, new String[]{NOTE_ID});
        UUID noteId = (UUID) holder.getKeys().get(NOTE_ID);
        return noteId;
    }

    @Transactional
    @Override
    public UUID create(byte[] file, String name, UUID user_id, UUID subjectId, String category, UUID parentId, String file_type) {
        final Map<String, Object> args = new HashMap<>();
        args.put(NOTE_NAME, name);
        args.put(FILE, file);
        args.put(SUBJECT_ID, subjectId);
        args.put(CATEGORY, category.toLowerCase());
        args.put(PARENT_ID, parentId);
        args.put(USER_ID, user_id);
        args.put(FILE_TYPE, file_type);

        UUID noteId = (UUID) jdbcNoteInsert.executeAndReturnKeyHolder(args).getKeys().get(NOTE_ID);
        return noteId;
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        return jdbcTemplate.query(
                "SELECT DISTINCT n.note_id, n.note_name, n.parent_id, n.category, n.created_at, n.last_modified_at, n.avg_score, n.file_type, " +
                        "u.user_id, u.email " +
                        "s.subject_id, s.subject_name, s.root_directory_id, " +
                        "FROM Notes n LEFT JOIN Reviews r ON n.note_id = r.note_id JOIN Subjects s ON n.subject_id = s.subject_id JOIN Users u ON n.user_id = u.user_id " +
                        "WHERE n.note_id = ? GROUP BY n.note_id, s.subject_id",
                ROW_MAPPER,
                noteId
        ).stream().findFirst();
    }

    @Override
    public byte[] getNoteFileById(UUID noteId){
        final byte[] file = jdbcTemplate.queryForObject("SELECT file FROM Notes WHERE note_id = ?",
                new Object[]{noteId}, (rs, rowNum) -> (byte[]) rs.getObject(FILE));
        // TODO: Move mapper to a constant?
        return file;
    }

    //@Transactional
    @Override
    public Review createOrUpdateReview(UUID noteId, UUID userId, Integer score, String content) {
        try {
            jdbcReviewInsert.execute(new HashMap<String, Object>(){{
                put(NOTE_ID, noteId);
                put(USER_ID, userId);
                put(SCORE, score);
                put(CONTENT, content);
            }});
        } catch (DuplicateKeyException e) {
            jdbcTemplate.update("UPDATE Reviews SET score = ?, content = ? WHERE note_id = ? AND user_id = ?", score, content, noteId, userId);
        } catch (DataIntegrityViolationException e) {
            //TODO: create custom exception
            throw e;
        }
        return jdbcTemplate.queryForObject(
                "SELECT u.user_id, u.email, r.score, r.content, n.note_id, n.note_name, o.user_id AS owner_id, o.email AS owner_email FROM Reviews r " +
                        "INNER JOIN Users u ON r.user_id = u.user_id " +
                        "INNER JOIN Notes n ON r.note_id = n.note_id " +
                        "INNER JOIN Users o ON n.user_id = o.user_id " +
                        "WHERE r.note_id = ? AND r.user_id = ?",
                new Object[]{noteId, userId},
                COMPLETE_REVIEW_ROW_MAPPER
        );
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
