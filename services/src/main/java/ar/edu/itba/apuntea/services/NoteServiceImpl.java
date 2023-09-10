package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.models.SearchArguments;
import ar.edu.itba.apuntea.persistence.NoteDao;
import ar.edu.itba.apuntea.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteDao noteDao;
    private final UserDao userDao;

    @Autowired
    public NoteServiceImpl(final NoteDao noteDao, final UserDao userDao) {
        this.noteDao = noteDao;
        this.userDao = userDao;
    }

    @Override
    public List<Note> searchNotes(String institution, String career, String subject, String category, Float score, String sortBy, boolean ascending, Integer page, Integer pageSize) {
//        try {
            SearchArguments sa = new SearchArguments(institution, career, subject, category, score, sortBy, ascending, page, pageSize);
            return noteDao.search(sa);
//        } catch (Exception e) { // TODO: Replace with more accurate exception
//            System.out.println(e.getMessage());
//        }
//        return new ArrayList<>();
    }

    @Override
    public Note createNote(MultipartFile file, String name, String email, UUID subjectId, String category) {
        UUID userId = userDao.createIfNotExists(email).getUserId();
        return noteDao.create(file, name, userId, subjectId, category);
    }

    @Override
    public byte[] getNoteFileById(UUID noteId) {
        return noteDao.getNoteFileById(noteId);
    }

}
