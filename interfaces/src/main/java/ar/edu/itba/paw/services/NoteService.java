package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    List<Note> searchNotes(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, Integer page, Integer pageSize);

    UUID createNote(MultipartFile file, String name, String email, UUID subjectId, String category);

    UUID createNote(MultipartFile file, String name, String email, UUID subjectId, String category, UUID parentId);

    Optional<Note> getNoteById(UUID noteId);

    byte[] getNoteFileById(UUID noteId);

    Integer createOrUpdateReview(UUID noteId, UUID userId, Integer score, String content);

    //TODO remove when users are implemented
    Integer createOrUpdateReview(UUID noteId, String email, Integer score, String content);

    void delete(UUID noteId);

    List<Note> getNotesByParentDirectory(UUID directoryId);

    List<Review> getReviews(UUID noteId);
}
