package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.persistence.NoteDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
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
    public List<Note> searchNotes(UUID institutionId, UUID careerId, UUID subjectId, String category, Float score, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        SearchArguments sa = new SearchArguments(institutionId, careerId, subjectId, category, score, word, sortBy, ascending, page, pageSize);
        return noteDao.search(sa);
    }

    @Override
    public Note createNote(MultipartFile file, String name, String email, UUID subjectId, String category) {
        UUID userId = userDao.createIfNotExists(email).getUserId();
        return noteDao.create(file, name, userId, subjectId, category);
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        return noteDao.getNoteById(noteId);
    }

    @Override
    public byte[] getNoteFileById(UUID noteId) {
        return noteDao.getNoteFileById(noteId);
    }

    @Override
    public Integer createOrUpdateReview(UUID noteId, UUID userId, Integer score) {
        return noteDao.createOrUpdateReview(noteId, userId, score);
    }
    @Override
    public Integer createOrUpdateReview(UUID noteId, String email, Integer score) {
        UUID userId = userDao.createIfNotExists(email).getUserId();
        return createOrUpdateReview(noteId, userId, score);
    }


}
