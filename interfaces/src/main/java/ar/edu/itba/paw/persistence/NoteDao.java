package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteDao {
    UUID create(String name, UUID subjectId, UUID userId, UUID parentId, boolean visible, byte[] file, String category, String fileType);

    UUID create(String name, UUID subjectId, UUID userId, boolean visible, byte[] file, String category, String fileType);

    Optional<byte[]> getNoteFileById(UUID noteId, UUID currentUserId);

    Review createOrUpdateReview(UUID noteId, UUID userId, Integer score, String content);

    Optional<Note> getNoteById(UUID noteId, UUID currentUserId);

    boolean update(Note note, UUID currentUserId);

    boolean delete(UUID noteId, UUID currentUserId);

    List<Review> getReviews(UUID noteId);
}