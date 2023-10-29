package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteDao {
    UUID create(String name, UUID subjectId, User user, UUID parentId, boolean visible, byte[] file, String category, String fileType);

    Optional<NoteFile> getNoteFileById(UUID noteId, UUID currentUserId);


    Optional<Note> getNoteById(UUID noteId, UUID currentUserId);

    boolean delete(List<UUID> noteId);
    boolean delete(List<UUID> noteIds, UUID currentUserId);

    List<Review> getReviews(UUID noteId);
    boolean deleteReview(UUID noteId, UUID userId);
    Review getReview(UUID noteId, UUID userId);
    void createOrUpdateReview(UUID noteId, UUID userId, int score, String content);

    List<Note> findNoteByIds(List<UUID> noteIds);
}