package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoteService {
    List<Note> search(String university, String career, String subject, String type, float score);

    Note create(MultipartFile multipartFile, String university, String career, String subject, String type);

}
