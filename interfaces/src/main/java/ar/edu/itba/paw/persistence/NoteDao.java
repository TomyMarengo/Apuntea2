package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.NoteFile;
import ar.edu.itba.paw.models.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteDao {
    UUID create(String name, UUID subjectId, UUID userId, UUID parentId, boolean visible, byte[] file, String category, String fileType);

    UUID create(String name, UUID subjectId, UUID userId, boolean visible, byte[] file, String category, String fileType);

    Optional<NoteFile> getNoteFileById(UUID noteId, UUID currentUserId);


    Optional<Note> getNoteById(UUID noteId, UUID currentUserId);

    boolean update(Note note, UUID currentUserId);

    List<Note> delete(UUID[] noteId);
    boolean delete(UUID[] noteIds, UUID currentUserId);

    List<Review> getReviews(UUID noteId);
    boolean deleteReview(UUID noteId, UUID userId);
    Review getReview(UUID noteId, UUID userId);
    void createOrUpdateReview(UUID noteId, UUID userId, Integer score, String content);
}