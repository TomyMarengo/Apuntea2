package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface NoteDao {
    Note create(MultipartFile multipartFile, String name);
    byte[] getNoteFileById(UUID noteId);
}
