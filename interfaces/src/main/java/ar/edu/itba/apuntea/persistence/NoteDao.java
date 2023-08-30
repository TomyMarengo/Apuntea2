package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.web.multipart.MultipartFile;

public interface NoteDao {
    Note create(MultipartFile multipartFile, String university, String career, String subject, String type);
}
