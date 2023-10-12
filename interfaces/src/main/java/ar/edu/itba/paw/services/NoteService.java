package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.NoteFile;
import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Review;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    UUID createNoteWithSubject(String name, UUID subjectId, boolean visible, MultipartFile file, String category) throws IOException;
    UUID createNote(String name, UUID parentId, boolean visible, MultipartFile file, String category) throws IOException;
    Optional<Note> getNoteById(UUID noteId);
    Optional<NoteFile> getNoteFileById(UUID noteId);
    void createOrUpdateReview(UUID noteId, int score, String content);
    void update(Note note);
    void delete(UUID[] noteIds, String reason);
    List<Review> getReviews(UUID noteId);
    void deleteReview(UUID noteId, UUID userId, String reason);
}
