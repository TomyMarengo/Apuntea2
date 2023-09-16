package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.SearchArguments;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteDao {
    Note create(byte[] file, String name, UUID userId, UUID subjectId, String category);

    Note create(byte[] file, String name, UUID userId, UUID subjectId, String category, UUID parentId);

    byte[] getNoteFileById(UUID noteId);

    List<Note> search(SearchArguments sa);

    List<Note> getNotesByParentDirectoryId(UUID directory_id);

    Integer createOrUpdateReview(UUID noteId, UUID userId, Integer score);

    Optional<Note> getNoteById(UUID noteId);

    void delete(UUID noteId);
}