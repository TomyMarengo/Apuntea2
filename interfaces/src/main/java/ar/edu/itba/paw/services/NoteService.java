package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Note;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    List<Note> searchNotes(UUID institutionId, UUID careerId, UUID subjectId, String category, Float score, String word, String sortBy, boolean ascending, Integer page, Integer pageSize);

    Note createNote(MultipartFile file, String name, String email, UUID subjectId, String category);

    Note createNote(MultipartFile file, String name, String email, UUID subjectId, String category, UUID parentId);

    Optional<Note> getNoteById(UUID noteId);

    byte[] getNoteFileById(UUID noteId);

    Integer createOrUpdateReview(UUID noteId, UUID userId, Integer score);

    //TODO remove when users are implemented
    Integer createOrUpdateReview(UUID noteId, String email, Integer score);

    void delete(UUID noteId);

    List<Note> getNotesByParentDirectory(UUID directoryId);
}
