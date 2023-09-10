package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    List<Note> searchNotes(String institution, String career, String subject, String category, Float score, String sortBy, boolean ascending, Integer page, Integer pageSize);

    Note createNote(MultipartFile file, String name, String email, UUID subjectId, String category);

    byte[] getNoteFileById(UUID noteId);
}
