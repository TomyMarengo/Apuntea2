package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.note.InvalidNoteException;
import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
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
    public UUID createNote(String name, UUID parentId, boolean visible, MultipartFile file, String category) {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        UUID subjectId = directoryDao.getDirectoryPath(parentId)
                                        .getRootDirectory()
                                        .getSubject()
                                        .getSubjectId();
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new InvalidFileException();
        }
        return noteDao.create(name, subjectId, userId, parentId, visible, fileBytes, category, FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    @Transactional
    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        final UUID userId = securityService.getCurrentUser().map(User::getUserId).orElse(null);
        return noteDao.getNoteById(noteId, userId);
    }

    @Transactional
    @Override
    public Optional<NoteFile> getNoteFileById(UUID noteId) {
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
    public void delete(UUID[] noteId, String reason) {
        User currentUser = securityService.getCurrentUserOrThrow();
        if (!currentUser.getIsAdmin()) {
            if (!noteDao.delete(noteId, currentUser.getUserId()))
                throw new InvalidNoteException();
        } else {
            List<Note> note = noteDao.delete(noteId);
            if (note.isEmpty()) throw new InvalidNoteException();
            note.forEach(n -> emailService.sendDeleteNoteEmail(n, reason));
        }
    }

    @Transactional
    @Override
    public List<Review> getReviews(UUID noteId) {
        return noteDao.getReviews(noteId);
    }

    @Transactional
    @Override
    public void createOrUpdateReview(UUID noteId, int score, String content) {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        noteDao.createOrUpdateReview(noteId, userId, score, content);
        Review review = noteDao.getReview(noteId, userId);
        if (review.getNote().getUser().getUserId().equals(userId))
            throw new InvalidReviewException();
        emailService.sendReviewEmail(review);
    }

    @Transactional
    @Override
    public void deleteReview(UUID noteId, UUID userId, String reason) {
        Review review = noteDao.getReview(noteId, userId);
        if (!noteDao.deleteReview(noteId, userId)) throw new InvalidReviewException();
        emailService.sendDeleteReviewEmail(review, reason);
    }
}
