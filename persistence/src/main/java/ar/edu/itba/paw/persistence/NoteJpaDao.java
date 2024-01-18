package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.*;
import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.search.SortArguments;
import ar.edu.itba.paw.models.user.User;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.models.NameConstants.*;
import static ar.edu.itba.paw.models.NameConstants.NAME;

@Repository
public class NoteJpaDao implements NoteDao {
    @PersistenceContext
    private EntityManager em;

    private final Logger LOGGER = LoggerFactory.getLogger(NoteJpaDao.class);

    @Override
    public UUID create(String name, UUID subjectId, User user, UUID parentId, boolean visible, byte[] file, String category, String fileType) {
        Note note = new Note.NoteBuilder()
                .name(name)
                .subject(em.getReference(Subject.class, subjectId))
                .parentId(parentId)
                .user(user)
                .visible(visible)
                .category(Category.valueOf(category.toUpperCase()))
                .fileType(fileType)
                .lastModifiedAt(LocalDateTime.now())
                .build();

        em.persist(note);
        NoteFile noteFile = new NoteFile(file, note);
        em.persist(noteFile);
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
    public boolean delete(UUID noteId) {
        return em.createQuery("DELETE FROM Note n WHERE n.id = :noteId")
                .setParameter("noteId", noteId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean delete(UUID noteId, UUID currentUserId) {
        return em.createQuery("DELETE FROM Note n WHERE n.id = :noteId AND n.user.id = :currentUserId")
                .setParameter("noteId", noteId)
                .setParameter("currentUserId", currentUserId)
                .executeUpdate() == 1;
    }

    @Override
    public int countReviewsByTargetUser(UUID targetUserId) {
        return ((BigInteger)em.createNativeQuery("SELECT COUNT(*) FROM Reviews r INNER JOIN Notes n ON r.note_id = n.note_id WHERE n.user_id = :userId")
                .setParameter("userId", targetUserId)
                .getSingleResult()).intValue();
    }

    @Override
    public List<Review> getReviewsByTargetUser(UUID targetUserId, int pageNum, int pageSize) {
        List<Review.ReviewKey> keys =  ((List<Object[]>)em.createNativeQuery("SELECT CAST(r.user_id AS VARCHAR(36)), " +
                        "CAST(r.note_id AS VARCHAR(36)) FROM Reviews r INNER JOIN Notes n ON r.note_id = n.note_id WHERE n.user_id = :userId ORDER BY r.created_at DESC")
                .setParameter("userId", targetUserId)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList()).stream()
                .map(o -> new Review.ReviewKey(
                             UUID.fromString((String) o[0]),
                             UUID.fromString((String) o[1])
                        )
                ).collect(Collectors.toList());
        if (keys.isEmpty()) return Collections.emptyList();
        return em.createQuery("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.note WHERE r.id IN :keys ORDER BY r.createdAt DESC", Review.class)
                .setParameter("keys", keys)
                .getResultList();
    }

    @Override
    public Optional<Review> getReview(UUID noteId, UUID userId)  {
        return em.createQuery("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.note WHERE r.note.id = :noteId AND r.user.id = :userId", Review.class)
                .setParameter("noteId", noteId)
                .setParameter("userId", userId)
                .getResultList().stream().findFirst();
    }

    @Override
    public int countReviews(UUID noteId, UUID userId) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM Reviews r WHERE TRUE ");
        if (noteId != null) queryBuilder.append("AND r.note_id = :noteId");
        if (userId != null) queryBuilder.append("AND r.user_id = :userId");
        final Query q = em.createNativeQuery(queryBuilder.toString());
        if (noteId != null) q.setParameter("noteId", noteId);
        if (userId != null) q.setParameter("userId", userId);
        return ((BigInteger)q.getSingleResult()).intValue();
    }


    // TODO: Check if all those join fetch are necessary
    @Override
    public List<Review> getReviews(UUID noteId, UUID userId, int pageNum, int pageSize) {
        StringBuilder queryBuilder = new StringBuilder("SELECT CAST(user_id AS VARCHAR(36)), CAST(note_id AS VARCHAR(36)) FROM Reviews WHERE TRUE ");
        if (noteId != null) queryBuilder.append("AND note_id = :noteId ");
        if (userId != null) queryBuilder.append("AND user_id = :userId ");
        queryBuilder.append("ORDER BY created_at DESC");

        Query q = em.createNativeQuery(queryBuilder.toString());
        if (noteId != null) q.setParameter("noteId", noteId);
        if (userId != null) q.setParameter("userId", userId);

        @SuppressWarnings("unchecked")
        List<Review.ReviewKey> keys = ((List<Object[]>)
                               q.setFirstResult((pageNum - 1) * pageSize)
                                .setMaxResults(pageSize)
                                .getResultList())
                .stream()
                .map(o -> new Review.ReviewKey(
                                UUID.fromString((String) o[0]),
                                UUID.fromString((String) o[1])
                        )
                ).collect(Collectors.toList());;

        if (keys.isEmpty()) return Collections.emptyList();
        return em.createQuery("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.note WHERE r.id IN :keys ORDER BY r.createdAt DESC", Review.class)
                .setParameter("keys", keys)
                .getResultList();
    }

    @Override
    public Review createOrUpdateReview(Note note, User user, int score, String content) {
        Review review = new Review(note, user, score, content);
        em.merge(review);
        return review;
    }

    @Override
    public boolean deleteReview(UUID noteId, UUID userId) {
        return em.createNativeQuery("DELETE FROM Reviews WHERE note_id = :noteId AND user_id = :userId")
                .setParameter("noteId", noteId)
                .setParameter("userId", userId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean addFavorite(UUID userId, UUID noteId) {
        return em.createNativeQuery("INSERT INTO Note_Favorites (user_id, note_id) SELECT :userId, :noteId WHERE NOT EXISTS (SELECT 1 FROM Note_Favorites WHERE user_id = :userId AND note_id = :noteId)")
                .setParameter("userId", userId)
                .setParameter("noteId", noteId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean removeFavorite(UUID userId, UUID noteId) {
        return em.createNativeQuery("DELETE FROM Note_Favorites WHERE user_id = :userId AND note_id = :noteId")
                .setParameter("userId", userId)
                .setParameter("noteId", noteId)
                .executeUpdate() == 1;
    }


    @Override
    public List<Note> findNotesByIds(List<UUID> noteIds, SortArguments sa) {
        if (noteIds.isEmpty()) return Collections.emptyList();
        return em.createQuery(String.format("SELECT n FROM Note n JOIN n.user u WHERE n.id IN :noteIds ORDER BY n.%s %s", DaoUtils.SORTBY_CAMELCASE.getOrDefault(sa.getSortBy(), "avgScore"), sa.isAscending()? "" : "DESC"), Note.class)
                .setParameter("noteIds", noteIds)
                .getResultList();
    }

    @Override
    public void loadNoteFavorites(List<UUID> noteIds, UUID currentUserId) {
        if (currentUserId == null) return;
        List<Note> favorites = em.createQuery("SELECT n FROM User u JOIN u.noteFavorites n WHERE n.noteId IN :noteIds AND u.userId = :userId", Note.class)
                    .setParameter("userId", currentUserId)
                    .setParameter("noteIds", noteIds)
                    .getResultList();
            favorites.forEach(note -> note.setFavorite(true));
    }



    @Override
    public List<Note> findNotesByIds(List<UUID> noteIds) {
        return findNotesByIds(noteIds, new SortArguments(SortArguments.SortBy.DATE, true));
    }

    @Override
    public void addInteractionIfNotExists(User user, Note note) {
        em.merge(new UserNoteInteraction(user, note));
    }

    @Override
    public List<Note> search(SearchArguments sa) {
        SortArguments sortArgs = sa.getSortArguments();

        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT DISTINCT CAST(id as VARCHAR(36))")
                .append(DaoUtils.SORTBY.getOrDefault(sortArgs.getSortBy(), NAME))
                .append(" FROM Normalized_Notes t ")
                .append("INNER JOIN Subjects s ON t.subject_id = s.subject_id ")
                .append("INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id ")
                .append("INNER JOIN Careers c ON sc.career_id = c.career_id ")
                .append("INNER JOIN Institutions i ON c.institution_id = i.institution_id ")
                .append("INNER JOIN Users u ON t.user_id = u.user_id ")
                .append("WHERE TRUE ");
        DaoUtils.applyInstitutionalFilters(queryCreator, sa);
        return getSearchResults(queryCreator, sa);
    }

    @Override
    public int countSearchResults(SearchArguments sa) {
        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT COUNT(DISTINCT id) FROM Normalized_Notes t ")
                .append("INNER JOIN Subjects s ON t.subject_id = s.subject_id ")
                .append("INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id ")
                .append("INNER JOIN Careers c ON sc.career_id = c.career_id ")
                .append("INNER JOIN Institutions i ON c.institution_id = i.institution_id ")
                .append("INNER JOIN Users u ON t.user_id = u.user_id ")
                .append("WHERE TRUE ");
        DaoUtils.applyInstitutionalFilters(queryCreator, sa);
        DaoUtils.applyGeneralFilters(queryCreator, sa);

        Query query = em.createNativeQuery(queryCreator.createQuery());
        queryCreator.getParams().forEach(query::setParameter);

        return ((BigInteger)query.getSingleResult()).intValue();
    }

    @Override
    public List<Note> navigate(SearchArguments sa) {
        SortArguments sortArgs = sa.getSortArguments();

        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT DISTINCT CAST(id as VARCHAR(36)) ")
                .append(DaoUtils.SORTBY.getOrDefault(sortArgs.getSortBy(), NAME))
                .append(" FROM Normalized_Notes t ")
                .appendIfPresent(sa.getFavBy(), "INNER JOIN Note_Favorites nf ON t.id = nf.note_id AND nf.user_id = :favBy ")
                .append("INNER JOIN Users u ON t.user_id = u.user_id WHERE TRUE ");
        queryCreator.addConditionIfPresent(PARENT_ID, "=", "AND", sa.getParentId());
        sa.getFavBy().ifPresent(favBy -> queryCreator.addParameter(FAV_BY, favBy));
        return getSearchResults(queryCreator, sa);
    }

    @Override
    public int countNavigationResults(SearchArguments sa) {
        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT COUNT(DISTINCT CAST(id as VARCHAR(36))) ")
                .append(" FROM Normalized_Notes t ")
                .appendIfPresent(sa.getFavBy(), "INNER JOIN Note_Favorites nf ON t.id = nf.note_id AND nf.user_id = :favBy ")
                .append("INNER JOIN Users u ON t.user_id = u.user_id WHERE TRUE ");
        queryCreator.addConditionIfPresent(PARENT_ID, "=", "AND", sa.getParentId());
        sa.getFavBy().ifPresent(favBy -> queryCreator.addParameter(FAV_BY, favBy));

        DaoUtils.applyGeneralFilters(queryCreator, sa);

        Query query = em.createNativeQuery(queryCreator.createQuery());
        queryCreator.getParams().forEach(query::setParameter);

        return ((BigInteger)query.getSingleResult()).intValue();
    }


    private List<Note> getSearchResults(DaoUtils.QueryCreator queryCreator, SearchArguments sa){
        SortArguments sortArgs = sa.getSortArguments();
        DaoUtils.applyGeneralFilters(queryCreator, sa);

        queryCreator.append("ORDER BY ").append(DaoUtils.SORTBY.getOrDefault(sortArgs.getSortBy(), NAME)).append(sortArgs.isAscending() ? "" : " DESC ");

        Query query = em.createNativeQuery(queryCreator.createQuery())
                .setFirstResult(sa.getPageSize() * (sa.getPage() - 1))
                .setMaxResults(sa.getPageSize());

        queryCreator.getParams().forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<UUID> noteIds = ((List<String>) query.getResultList()).stream().map(UUID::fromString).collect(Collectors.toList());

        return findNotesByIds(noteIds, sortArgs);
        // TODO: Polemicardo in the bar
        // sa.getCurrentUserId().ifPresent(uId -> directoryDao.loadDirectoryFavorites(directoryIds, uId));
    }
}
