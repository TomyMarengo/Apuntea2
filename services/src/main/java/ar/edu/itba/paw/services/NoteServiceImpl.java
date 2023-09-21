package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.persistence.NoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteDao noteDao;
    private final SecurityService securityService;
    private final EmailService emailService;

    @Autowired
    public NoteServiceImpl(final NoteDao noteDao, final SecurityService securityService, final EmailService emailService) {
        this.noteDao = noteDao;
        this.securityService = securityService;
        this.emailService = emailService;
    }

    @Override
    public List<Note> searchNotes(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        SearchArguments sa = new SearchArguments(institutionId, careerId, subjectId, category, word, sortBy, ascending, page, pageSize);
        return noteDao.search(sa);
    }

    @Transactional
    @Override
    public UUID createNote(MultipartFile file, String name, UUID subjectId, String category) throws IOException {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        return noteDao.create(file.getBytes(), name, userId, subjectId, category);
    }

    @Transactional
    @Override
    public UUID createNote(MultipartFile file, String name, UUID subjectId, String category, UUID parentId) throws IOException {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        return noteDao.create(file.getBytes(), name, userId, subjectId, category, parentId);
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
    public Integer createOrUpdateReview(UUID noteId, Integer score, String content) {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        Review review = noteDao.createOrUpdateReview(noteId, userId, score, content);
        emailService.sendReviewEmail(review);
        return review.getScore();
    }

    @Transactional
    @Override
    public void delete(UUID noteId) {
        noteDao.delete(noteId);
    }

    @Override
    public List<Note> getNotesByParentDirectory(UUID directoryId) {
        return noteDao.getNotesByParentDirectoryId(directoryId);
    }

    @Override
    public List<Review> getReviews(UUID noteId) {
        return noteDao.getReviews(noteId);
    }

}
