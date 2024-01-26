package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;

import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    UUID createNote(final String name, final UUID parentId, final boolean visible, final byte[] file, final String mimeType, final String category);

    Optional<Note> getNoteById(UUID noteId);

    Optional<NoteFile> getNoteFileById(UUID noteId);

    Page<Note> getNotes(UUID parentId, UUID userId, UUID favBy, String category, String word,
                        UUID institutionId, UUID careerId, UUID subjectId,
                        String sortBy, boolean ascending, int page, int pageSize);

    Review createReview(UUID noteId, int score, String content);

    Review updateReview(UUID noteId, int score, String content);

    void update(UUID noteId, String name, Boolean visible, String category);

    void delete(UUID noteId, String reason);

    Optional<Review> getReview(UUID noteId, UUID userId);

    Page<Review> getReviews(UUID noteId, UUID userId, int pageNum, int pageSize);

    Page<Review> getReviewsByTargetUser(UUID targetUserId, int pageNum, int pageSize);

    boolean deleteReview(UUID noteId, UUID userId, String reason);

    //    Collection<Note> getFavorites();
    boolean isFavorite(UUID noteId);

    boolean addFavorite(UUID noteId);

    boolean removeFavorite(UUID noteId);

    void addInteraction(UUID noteId);

}
