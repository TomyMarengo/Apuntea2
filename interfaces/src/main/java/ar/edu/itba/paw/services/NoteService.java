package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    UUID createNote(String name, UUID parentId, boolean visible, MultipartFile file, String category);
    Optional<Note> getNoteById(UUID noteId);
    Optional<NoteFile> getNoteFileById(UUID noteId);
    void createOrUpdateReview(UUID noteId, int score, String content);
    void update(UUID noteId, String name, boolean visible, String category);
    void delete(UUID[] noteIds, String reason);
    Page<Review> getPaginatedReviews(UUID note, int pageNum, int pageSize);
    List<Review> getReviews(UUID noteId);
    void deleteReview(UUID noteId, UUID userId, String reason);
    Collection<Note> getFavorites();
    void addFavorite(UUID noteId);
    void removeFavorite(UUID noteId);
}
