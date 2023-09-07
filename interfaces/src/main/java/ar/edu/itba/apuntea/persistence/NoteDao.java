package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.models.SearchArguments;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface NoteDao {
    Note create(MultipartFile multipartFile, String name);

    byte[] getNoteFileById(UUID noteId);

    List<Note> search(SearchArguments sa);
}
