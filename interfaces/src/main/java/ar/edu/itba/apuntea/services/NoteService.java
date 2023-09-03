package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    List<Note> search(String university, String career, String subject, String type, float score);

    Note create(MultipartFile multipartFile, String university, String career, String subject, String type);

    byte[] getNoteFileById(UUID noteId);
}
