package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    UUID createNote(final String name, final UUID parentId, final boolean visible, final byte[] file, final String mimeType, final String category);
    Optional<Note> getNoteById(UUID noteId);
    Optional<NoteFile> getNoteFileById(UUID noteId);
    Page<Note> getNotes(UUID parentId, UUID userId, UUID favBy, String category, String word, String sortBy, boolean ascending, int page, int pageSize);
    void createOrUpdateReview(UUID noteId, int score, String content);
    void update(UUID noteId, String name, Boolean visible, String category);
    void delete(UUID noteId, String reason);
    Page<Review> getReviews(UUID noteId, int pageNum, int pageSize);
    Page<Review> getReviewsDoneToUser(UUID userId, int pageNum, int pageSize);
    List<Review> getReviews(UUID noteId);
    void deleteReview(UUID noteId, UUID userId, String reason);
    Collection<Note> getFavorites();
    boolean addFavorite(UUID noteId);
    boolean removeFavorite(UUID noteId);
}
