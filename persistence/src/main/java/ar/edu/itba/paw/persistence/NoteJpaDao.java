package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ar.edu.itba.paw.models.NameConstants.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class NoteJpaDao implements NoteDao {
    @PersistenceContext
    private EntityManager em;

    private final Logger LOGGER = LoggerFactory.getLogger(NoteJpaDao.class);
    private static final int REVIEW_LIMIT = 10;

    @Override
    public UUID create(String name, UUID subjectId, User user, UUID parentId, boolean visible, byte[] file, String category, String fileType) {
        Note note = new Note.NoteBuilder()
                .name(name)
                .subjectId(subjectId)
                .parentId(parentId)
                .user(user)
                .visible(visible)
                .category(Category.valueOf(category.toUpperCase()))
                .fileType(fileType)
                .createdAt(LocalDateTime.now()) // TODO: See if it is possible to remove this
                .lastModifiedAt(LocalDateTime.now())
                .build();

        NoteFile noteFile = new NoteFile(file, note);
        note.setNoteFile(noteFile);

        em.persist(note);
        return note.getId();
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId, UUID currentUserId) {
        return em.createQuery("SELECT n FROM Note n WHERE n.id = :noteId AND (n.visible = true OR n.user.id = :currentUserId)", Note.class)
                .setParameter("noteId", noteId)
                .setParameter("currentUserId", currentUserId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<NoteFile> getNoteFileById(UUID noteId, UUID currentUserId){
        return em.createQuery("SELECT nf FROM NoteFile nf WHERE nf.note.id = :noteId AND (nf.note.visible = true OR nf.note.user.id = :currentUserId)", NoteFile.class)
                .setParameter("noteId", noteId)
                .setParameter("currentUserId", currentUserId)
                .getResultList()
                .stream()
                .findFirst();

//        return namedParameterJdbcTemplate.query("SELECT file, file_type FROM Notes n WHERE note_id = :note_id AND ( visible " + getVisibilityCondition(currentUserId, args) + ")",
//                args, NOTE_FILE_ROW_MAPPER).stream().findFirst();
//        return Optional.empty();
    }


    @Override
    public List<Note> delete(UUID[] noteIds) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(NOTE_ID, Arrays.asList(noteIds));
//        List<Note> notes = new ArrayList<>(
//                namedParameterJdbcTemplate.query("SELECT n.note_id, n.note_name, n.category, n.file_type, " +
//                                "u.user_id, u.email, u.locale FROM Notes n INNER JOIN Users u ON n.user_id = u.user_id WHERE note_id IN (:note_id)",
//                        args, USER_ROW_MAPPER)
//        );
//        int rowsDeleted = namedParameterJdbcTemplate.update("DELETE FROM Notes WHERE note_id IN (:note_id)", args);
//        if (rowsDeleted == 0) return Collections.emptyList();
//        return notes;
        return Collections.emptyList();
    }

    @Override
    public boolean delete(UUID[] noteIds, UUID currentUserId) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(NOTE_ID, Arrays.asList(noteIds));
//        args.addValue(USER_ID, currentUserId);
//        int deleted = namedParameterJdbcTemplate.update("DELETE FROM Notes WHERE note_id IN (:note_id) AND user_id = :user_id", args);
//        return deleted == noteIds.length;
        return false;
    }

    @Override
    public boolean update(Note note, UUID currentUserId) {
//        return jdbcTemplate.update("UPDATE Notes SET note_name = ?, category = ?, visible = ?, last_modified_at = now() WHERE note_id = ? AND user_id = ?",
//                note.getName(), note.getCategory().toString().toLowerCase(), note.isVisible(), note.getId(), currentUserId) == 1;
        return false;
    }

    private String getVisibilityCondition(UUID currentUserId, MapSqlParameterSource args) {
        if (currentUserId != null) args.addValue(USER_ID, currentUserId);
        return currentUserId != null? "OR n.user_id = :user_id" : "";
    }

    @Override
    public Review getReview(UUID noteId, UUID userId)  {
//        return jdbcTemplate.queryForObject(
//                "SELECT u.user_id, u.email, u.locale, r.score, r.content, n.note_id, n.note_name, o.user_id AS owner_id, o.email AS owner_email, o.locale as owner_locale FROM Reviews r " +
//                        "INNER JOIN Users u ON r.user_id = u.user_id " +
//                        "INNER JOIN Notes n ON r.note_id = n.note_id " +
//                        "INNER JOIN Users o ON n.user_id = o.user_id " +
//                        "WHERE r.note_id = ? AND r.user_id = ?",
//                new Object[]{noteId, userId},
//                COMPLETE_REVIEW_ROW_MAPPER
//        );
        return null;
    }

    @Override
    public List<Review> getReviews(UUID noteId) {
//        // Right now it's not necessary to validate that the current user has visibility enabled
//        return jdbcTemplate.query(
//                "SELECT u.user_id, u.email, r.score, r.content, r.created_at FROM Reviews r INNER JOIN Users u ON r.user_id = u.user_id WHERE r.note_id = ? ORDER BY r.created_at DESC LIMIT " + REVIEW_LIMIT,
//                    new Object[]{noteId},
//                    REVIEW_ROW_MAPPER
//        );
        return Collections.emptyList();
    }

    @Override
    public void createOrUpdateReview(UUID noteId, UUID userId, int score, String content) {
//        boolean success = jdbcTemplate.update("UPDATE Reviews SET score = ?, content = ?, created_at = now() WHERE note_id = ? AND user_id = ?", score, content, noteId, userId) == 1;
//        if (!success) {
//            jdbcReviewInsert.execute(new HashMap<String, Object>(){{
//                put(NOTE_ID, noteId);
//                put(USER_ID, userId);
//                put(SCORE, score);
//                put(CONTENT, content);
//            }});
//        }
    }

    @Override
    public boolean deleteReview(UUID noteId, UUID userId) {
//        return jdbcTemplate.update("DELETE FROM Reviews WHERE note_id = ? AND user_id = ?", noteId, userId) == 1;
        return false;
    }

    @Override
    public List<Note> findNoteByIds(List<UUID> noteIds) {
        if (noteIds.isEmpty()) return Collections.emptyList();
        return em.createQuery("FROM Note n WHERE n.id IN :noteIds", Note.class)
                .setParameter("noteIds", noteIds)
                .getResultList();
    }

}
