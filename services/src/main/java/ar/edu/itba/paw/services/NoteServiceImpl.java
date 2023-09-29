package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.InvalidNoteException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.NoteDao;
import org.apache.commons.io.FilenameUtils;
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
    private final DirectoryDao directoryDao;
    private final EmailService emailService;
    private final SecurityService securityService;

    @Autowired
    public NoteServiceImpl(final NoteDao noteDao, final DirectoryDao directoryDao, final SecurityService securityService, final EmailService emailService) {
        this.noteDao = noteDao;
        this.securityService = securityService;
        this.emailService = emailService;
        this.directoryDao = directoryDao;
    }

    @Transactional
    @Override
    public UUID createNoteWithSubject(String name, UUID subjectId, boolean visible, MultipartFile file, String category) throws IOException {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        return noteDao.create(name, subjectId, userId, visible, file.getBytes(), category, FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    @Transactional
    @Override
    public UUID createNote(String name, UUID parentId, boolean visible, MultipartFile file, String category) throws IOException {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        UUID subjectId = directoryDao.getDirectoryPath(parentId)
                                        .getRootDirectory()
                                        .getSubject()
                                        .getSubjectId();
        return noteDao.create(name, subjectId, userId, parentId, visible, file.getBytes(), category, FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        final UUID userId = securityService.getCurrentUser().map(User::getUserId).orElse(null);
        return noteDao.getNoteById(noteId, userId);
    }

    @Override
    public Optional<byte[]> getNoteFileById(UUID noteId) {
        final UUID userId = securityService.getCurrentUser().map(User::getUserId).orElse(null);
        return noteDao.getNoteFileById(noteId, userId);
    }

    @Transactional
    @Override
    public void update(Note note) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = noteDao.update(note, currentUserId);
        if (!success) throw new InvalidNoteException();
    }


    @Transactional
    @Override
    public void delete(UUID noteId) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = noteDao.delete(noteId, currentUserId);
        if (!success) throw new InvalidNoteException();
    }

    @Transactional
    @Override
    public void deleteMany(UUID[] noteIds) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = noteDao.deleteMany(noteIds, currentUserId);
        if (!success) throw new InvalidNoteException();
    }

    @Override
    public List<Review> getReviews(UUID noteId) {
        return noteDao.getReviews(noteId);
    }

    @Override
    public Integer createOrUpdateReview(UUID noteId, Integer score, String content) {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        Review review = noteDao.createOrUpdateReview(noteId, userId, score, content);
        emailService.sendReviewEmail(review);
        return review.getScore();
    }
}
