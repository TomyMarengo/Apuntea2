package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.Review;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    UUID createNote(String name, UUID parentId, boolean visible, MultipartFile file, String category);
    Optional<Note> getNoteById(UUID noteId);
    Optional<NoteFile> getNoteFileById(UUID noteId);
    void createOrUpdateReview(UUID noteId, int score, String content);
    void update(Note note);
    void delete(UUID[] noteIds, String reason);
    List<Review> getReviews(UUID noteId);
    void deleteReview(UUID noteId, UUID userId, String reason);
}
