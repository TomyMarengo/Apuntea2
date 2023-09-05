package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.persistence.NoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService{
    private final NoteDao noteDao;

    @Autowired
    public NoteServiceImpl(final NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public List<Note> search(String university, String career, String subject, String type, float score) {
        return null;
    }

    @Override
    public Note create(MultipartFile multipartFile, String name) {
        return noteDao.create(multipartFile, name);
    }

    @Override
    public byte[] getNoteFileById(UUID noteId) {
        return noteDao.getNoteFileById(noteId);
    }

}
