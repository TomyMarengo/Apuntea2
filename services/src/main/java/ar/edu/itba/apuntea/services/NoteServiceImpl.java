package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.models.SearchArguments;
import ar.edu.itba.apuntea.persistence.NoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public List<Note> search(String institution, String career, String subject, String category, Float score, String sortBy, boolean ascending, Integer page, Integer pageSize) {
//        try {
            SearchArguments sa = new SearchArguments(institution, career, subject, category, score, sortBy, ascending, page, pageSize);
            return noteDao.search(sa);
//        } catch (Exception e) { // TODO: Replace with more accurate exception
//            System.out.println(e.getMessage());
//        }
//        return new ArrayList<>();
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
