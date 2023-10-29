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
    }


    @Override
    public boolean delete(List<UUID> noteIds) {
        if (noteIds.isEmpty()) return false;
        return em.createQuery("DELETE FROM Note n WHERE n.id IN :noteIds")
                .setParameter("noteIds", noteIds)
                .executeUpdate() == noteIds.size();
    }

    @Override
    public boolean delete(List<UUID> noteIds, UUID currentUserId) {
        if (noteIds.isEmpty()) return false;
        return em.createQuery("DELETE FROM Note n WHERE n.id IN :noteIds AND n.user.id = :currentUserId")
                .setParameter("noteIds", noteIds)
                .setParameter("currentUserId", currentUserId)
                .executeUpdate() == noteIds.size();
    }

    @Override
    public Review getReview(UUID noteId, UUID userId)  {
        return em.createQuery("FROM Review r WHERE r.note.id = :noteId AND r.user.id = :userId", Review.class)
                .setParameter("noteId", noteId)
                .setParameter("userId", userId)
                .getSingleResult();
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
    public Review createOrUpdateReview(Note note, User user, int score, String content) {
        Review review = new Review(note, user, content, score);
        em.merge(review);
        return review;
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
        return em.createQuery("DELETE FROM Review r WHERE r.note.id = :noteId AND r.user.id = :userId")
                .setParameter("noteId", noteId)
                .setParameter("userId", userId)
                .executeUpdate() == 1;
    }

    @Override
    public List<Note> findNoteByIds(List<UUID> noteIds) {
        if (noteIds.isEmpty()) return Collections.emptyList();
        return em.createQuery("FROM Note n WHERE n.id IN :noteIds", Note.class)
                .setParameter("noteIds", noteIds)
                .getResultList();
    }

}
