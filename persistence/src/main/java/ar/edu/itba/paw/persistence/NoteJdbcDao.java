package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
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

    private static final int REVIEW_LIMIT = 10;

    private final static RowMapper<Note> ROW_MAPPER = (rs, rowNum) ->
            new Note.NoteBuilder()
                    .noteId(UUID.fromString(rs.getString(NOTE_ID)))
                    .name(rs.getString(NOTE_NAME))
                    .user( new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL)
                    ))
                    .parentId(UUID.fromString(rs.getString(PARENT_ID)))
                    .subject(new Subject(
                            UUID.fromString(rs.getString(SUBJECT_ID)),
                            rs.getString(SUBJECT_NAME),
                            UUID.fromString(rs.getString(ROOT_DIRECTORY_ID))
                    ))
                    .category(Category.valueOf(rs.getString(CATEGORY).toUpperCase()))
                    .createdAt(rs.getTimestamp(CREATED_AT).toLocalDateTime())
                    .lastModifiedAt(rs.getTimestamp(LAST_MODIFIED_AT).toLocalDateTime())
                    .visible(rs.getBoolean(VISIBLE))
                    .fileType(rs.getString(FILE_TYPE))
                    .avgScore(rs.getFloat(AVG_SCORE))
                    .build();

    private final static RowMapper<Note> USER_ROW_MAPPER = (rs, rowNum) ->
            new Note.NoteBuilder()
                    .noteId(UUID.fromString(rs.getString(NOTE_ID)))
                    .name(rs.getString(NOTE_NAME))
                    .category(Category.valueOf(rs.getString(CATEGORY).toUpperCase()))
                    .fileType(rs.getString(FILE_TYPE))
                    .user( new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL),
                            rs.getString(LOCALE)
                    ))
                    .build();

    private final static RowMapper<Review> REVIEW_ROW_MAPPER = (rs, rowNum) ->
            new Review(
                    new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL)
                    ),
                    rs.getString(CONTENT),
                    rs.getInt(SCORE),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime()
            );

    private final static RowMapper<Review> COMPLETE_REVIEW_ROW_MAPPER = (rs, rowNum) ->
            new Review(
                    new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL),
                            rs.getString(LOCALE)
                    ),
                    rs.getString(CONTENT),
                    rs.getInt(SCORE),
                    new Note.NoteBuilder()
                        .noteId(UUID.fromString(rs.getString(NOTE_ID)))
                        .name(rs.getString(NOTE_NAME))
                        .user( new User(
                                UUID.fromString(rs.getString(OWNER_ID)),
                                rs.getString(OWNER_EMAIL),
                                rs.getString(OWNER_LOCALE)
                        ))
                        .build()
            );

    private final static RowMapper<NoteFile> NOTE_FILE_ROW_MAPPER = (rs, rowNum) ->
            new NoteFile(
                    rs.getString(FILE_TYPE),
                    rs.getBytes(FILE)
            );

    @Autowired
    public NoteJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcNoteInsert = new SimpleJdbcInsert(ds)
                .withTableName(NOTES)
                .usingGeneratedKeyColumns(NOTE_ID)
                .usingColumns(NOTE_NAME, FILE, SUBJECT_ID, CATEGORY, PARENT_ID, USER_ID, FILE_TYPE);
        this.jdbcReviewInsert = new SimpleJdbcInsert(ds)
                .withTableName(REVIEWS)
                .usingColumns(NOTE_ID, USER_ID, SCORE, CONTENT);
    }

    @Override
    public UUID create(String name, UUID subjectId, UUID userId, boolean visible, byte[] file, String category, String fileType) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_NAME, name);
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(USER_ID, userId);
        args.addValue(VISIBLE, visible);
        args.addValue(FILE, file);
        args.addValue(CATEGORY, category.toLowerCase());
        args.addValue(FILE_TYPE, fileType);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Notes (note_name, subject_id, user_id, parent_id, visible, file,  category, file_type) " +
                "SELECT :note_name, :subject_id, :user_id, s.root_directory_id, :visible, :file, :category, :file_type FROM Subjects s WHERE s.subject_id = :subject_id"
                , args, holder, new String[]{NOTE_ID});
        return (UUID) holder.getKeys().get(NOTE_ID);
    }

    @Override
    public UUID create(String name, UUID subjectId, UUID userId, UUID parentId, boolean visible, byte[] file, String category, String fileType) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_NAME, name);
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(USER_ID, userId);
        args.addValue(PARENT_ID, parentId);
        args.addValue(VISIBLE, visible);
        args.addValue(FILE, file);
        args.addValue(CATEGORY, category.toLowerCase());
        args.addValue(FILE_TYPE, fileType);
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Notes (note_name, subject_id, user_id, parent_id, visible, file, category, file_type)  " +
                        "SELECT :note_name, :subject_id, :user_id, d.directory_id, :visible, :file, :category, :file_type FROM Directories d " +
                        "WHERE d.directory_id = :parent_id AND (d.user_id = :user_id OR d.parent_id IS NULL)"
                , args, holder, new String[]{NOTE_ID});
        return (UUID) holder.getKeys().get(NOTE_ID);
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId, UUID currentUserId) {
        MapSqlParameterSource args = new MapSqlParameterSource(NOTE_ID, noteId);
        String query = "SELECT DISTINCT n.note_id, n.note_name, n.parent_id, n.category, n.created_at, n.last_modified_at, n.visible, COALESCE(AVG(r.score), 0) AS avg_score, n.file_type, " +
                        "u.user_id, u.email, " +
                        "s.subject_id, s.subject_name, s.root_directory_id " +
                        "FROM Notes n LEFT JOIN Reviews r ON n.note_id = r.note_id JOIN Subjects s ON n.subject_id = s.subject_id JOIN Users u ON n.user_id = u.user_id " +
                        "WHERE n.note_id = :note_id AND ( n.visible " + getVisibilityCondition(currentUserId, args) + ") GROUP BY n.note_id, u.user_id, s.subject_id";
        return namedParameterJdbcTemplate.query(query, args, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<NoteFile> getNoteFileById(UUID noteId, UUID currentUserId){
        MapSqlParameterSource args = new MapSqlParameterSource(NOTE_ID, noteId);
        return namedParameterJdbcTemplate.query("SELECT file, file_type FROM Notes n WHERE note_id = :note_id AND ( visible " + getVisibilityCondition(currentUserId, args) + ")",
                args, NOTE_FILE_ROW_MAPPER).stream().findFirst();
    }

    private String getVisibilityCondition(UUID currentUserId, MapSqlParameterSource args) {
        if (currentUserId != null) args.addValue(USER_ID, currentUserId);
        return currentUserId != null? "OR n.user_id = :user_id" : "";
    }

    @Override
    public boolean deleteReview(UUID noteId, UUID userId) {
        return jdbcTemplate.update("DELETE FROM Reviews WHERE note_id = ? AND user_id = ?", noteId, userId) == 1;
    }

    @Override
    public Review getReview(UUID noteId, UUID userId)  {
        return jdbcTemplate.queryForObject(
                "SELECT u.user_id, u.email, u.locale, r.score, r.content, n.note_id, n.note_name, o.user_id AS owner_id, o.email AS owner_email, o.locale as owner_locale FROM Reviews r " +
                        "INNER JOIN Users u ON r.user_id = u.user_id " +
                        "INNER JOIN Notes n ON r.note_id = n.note_id " +
                        "INNER JOIN Users o ON n.user_id = o.user_id " +
                        "WHERE r.note_id = ? AND r.user_id = ?",
                new Object[]{noteId, userId},
                COMPLETE_REVIEW_ROW_MAPPER
        );
    }

    @Override
    public void createOrUpdateReview(UUID noteId, UUID userId, Integer score, String content) {
        boolean success = jdbcTemplate.update("UPDATE Reviews SET score = ?, content = ?, created_at = now() WHERE note_id = ? AND user_id = ?", score, content, noteId, userId) == 1;
        if (!success) {
            jdbcReviewInsert.execute(new HashMap<String, Object>(){{
                put(NOTE_ID, noteId);
                put(USER_ID, userId);
                put(SCORE, score);
                put(CONTENT, content);
            }});
        }
    }

    @Override
    public List<Note> delete(UUID[] noteIds) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_ID, Arrays.asList(noteIds));
        List<Note> notes = new ArrayList<>(
                namedParameterJdbcTemplate.query("SELECT n.note_id, n.note_name, n.category, n.file_type, " +
                                "u.user_id, u.email, u.locale FROM Notes n INNER JOIN Users u ON n.user_id = u.user_id WHERE note_id IN (:note_id)",
                        args, USER_ROW_MAPPER)
        );
        int rowsDeleted = namedParameterJdbcTemplate.update("DELETE FROM Notes WHERE note_id IN (:note_id)", args);
        if (rowsDeleted == 0) return Collections.emptyList();
        return notes;
    }

    @Override
    public boolean delete(UUID[] noteIds, UUID currentUserId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_ID, Arrays.asList(noteIds));
        args.addValue(USER_ID, currentUserId);
        int deleted = namedParameterJdbcTemplate.update("DELETE FROM Notes WHERE note_id IN (:note_id) AND user_id = :user_id", args);
        return deleted == noteIds.length;
    }

    @Override
    public boolean update(Note note, UUID currentUserId) {
        return jdbcTemplate.update("UPDATE Notes SET note_name = ?, category = ?, visible = ?, last_modified_at = now() WHERE note_id = ? AND user_id = ?",
                    note.getName(), note.getCategory().toString().toLowerCase(), note.isVisible(), note.getId(), currentUserId) == 1;
    }

    @Override
    public List<Review> getReviews(UUID noteId) {
        // Right now it's not necessary to validate that the current user has visibility enabled
        return jdbcTemplate.query(
                "SELECT u.user_id, u.email, r.score, r.content, r.created_at FROM Reviews r INNER JOIN Users u ON r.user_id = u.user_id WHERE r.note_id = ? ORDER BY r.created_at DESC LIMIT " + REVIEW_LIMIT,
                    new Object[]{noteId},
                    REVIEW_ROW_MAPPER
        );
    }

}
