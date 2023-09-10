package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.SearchArguments;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface NoteDao {
    Note create(MultipartFile file, String name, UUID user_id, UUID subjectId, String category);

    byte[] getNoteFileById(UUID noteId);

    List<Note> search(SearchArguments sa);

    List<Note> getNotesByParentDirectoryId(UUID directory_id);
}
