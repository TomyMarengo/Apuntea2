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
    Integer createOrUpdateReview(UUID noteId, Integer score, String content);
    void update(Note note);
    void delete(UUID noteId);

    void deleteMany(UUID[] noteIds);
    List<Review> getReviews(UUID noteId);
}
