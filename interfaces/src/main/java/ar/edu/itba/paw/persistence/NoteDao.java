package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.search.SortArguments;
import ar.edu.itba.paw.models.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteDao {
    UUID create(String name, UUID subjectId, User user, UUID parentId, boolean visible, byte[] file, String category, String fileType);

    Optional<NoteFile> getNoteFileById(UUID noteId, UUID currentUserId);

    Optional<Note> getNoteById(UUID noteId, UUID currentUserId);

    boolean delete(UUID noteId);

    boolean delete(UUID noteId, UUID currentUserId);

    int countReviews(UUID noteId, UUID userId);

    int countReviewsByTargetUser(UUID targetUserId);

    List<Review> getReviewsByTargetUser(UUID targetUserId, int pageNum, int pageSize);

    List<Review> getReviews(UUID noteId, UUID userId, int pageNum, int pageSize);


    boolean deleteReview(UUID noteId, UUID userId);

    Optional<Review> getReview(UUID noteId, UUID userId);

    Review createOrUpdateReview(Note note, User user, int score, String content);

    boolean isFavorite(UUID userId, UUID noteId);

    boolean addFavorite(UUID userId, UUID noteId);

    boolean removeFavorite(UUID userId, UUID noteId);

    List<Note> findNotesByIds(List<UUID> noteIds);

    List<Note> findNotesByIds(List<UUID> noteIds, SortArguments sa);

    void loadNoteFavorites(List<UUID> noteIds, UUID currentUserId);

    void addInteractionIfNotExists(User user, Note note);

    List<Note> search(SearchArguments sa);
    List<Note> navigate(SearchArguments sa);

    int countSearchResults(SearchArguments sa);
    int countNavigationResults(SearchArguments sa);
}