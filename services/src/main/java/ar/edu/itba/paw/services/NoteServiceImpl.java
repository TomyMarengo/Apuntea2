package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.exceptions.note.InvalidNoteException;
import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.NoteDao;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteDao noteDao;
    private final DirectoryDao directoryDao;
    private final SubjectDao subjectDao;
    private final EmailService emailService;
    private final SecurityService securityService;

    @Autowired
    public NoteServiceImpl(final NoteDao noteDao, final DirectoryDao directoryDao, final SecurityService securityService, final EmailService emailService, final SubjectDao subjectDao) {
        this.noteDao = noteDao;
        this.securityService = securityService;
        this.emailService = emailService;
        this.directoryDao = directoryDao;
        this.subjectDao = subjectDao;
    }

    @Transactional
    @Override
    public UUID createNote(String name, UUID parentId, boolean visible, MultipartFile file, String category) {
        User user = securityService.getCurrentUserOrThrow();
        List<UUID> directoryPathIds = directoryDao.getDirectoryPathIds(parentId);
        if (directoryPathIds.isEmpty()) throw new InvalidDirectoryException();
        UUID rootDirectoryId = directoryPathIds.get(0);

        UUID subjectId = subjectDao.getSubjectByRootDirectoryId(rootDirectoryId);
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new InvalidFileException();
        }
        return noteDao.create(name, subjectId, user, parentId, visible, fileBytes, category, FilenameUtils.getExtension(file.getOriginalFilename()));
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
    public void update(UUID noteId, String name, boolean visible, String category) {
        Note note = noteDao.getNoteById(noteId, securityService.getCurrentUserOrThrow().getUserId()).orElseThrow(InvalidNoteException::new);
        // TODO: Should we move this to the Dao again for testing purposes?
        note.setName(name);
        note.setVisible(visible);
        note.setCategory(Category.valueOf(category.toUpperCase()));
        note.setLastModifiedAt(LocalDateTime.now());
    }


    @Transactional
    @Override
    public void delete(UUID[] noteIds, String reason) {
        if (noteIds.length == 0) return;

        // TODO: Propagate List<UUID> instead of UUID[] to the Controller?
        List<UUID> noteIdsList = Collections.unmodifiableList(Arrays.asList(noteIds));

        User currentUser = securityService.getCurrentUserOrThrow();
        if (!currentUser.getIsAdmin()) {
            if (!noteDao.delete(noteIdsList, currentUser.getUserId()))
                throw new InvalidNoteException();
        } else {
            List<Note> notes = noteDao.findNoteByIds(noteIdsList);
            if (notes.size() != noteIdsList.size() || !noteDao.delete(noteIdsList)) throw new InvalidNoteException();
            notes.forEach(n -> emailService.sendDeleteNoteEmail(n, reason));
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
        User user = securityService.getCurrentUserOrThrow();
        Note note = noteDao.getNoteById(noteId, user.getUserId()).orElseThrow(NoteNotFoundException::new);
        if (note.getUser().equals(user))
            throw new InvalidReviewException();
        Review review = noteDao.createOrUpdateReview(note, user, score, content);
        emailService.sendReviewEmail(review);
    }

    @Transactional
    @Override
    public void deleteReview(UUID noteId, UUID userId, String reason) {
        Review review = noteDao.getReview(noteId, userId);
        if (!noteDao.deleteReview(noteId, userId)) throw new InvalidReviewException();
        emailService.sendDeleteReviewEmail(review, reason);
    }

    @Transactional
    @Override
    public List<Note> getFavorites() {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        return noteDao.getFavorites(currentUserId);
    }

    @Transactional
    @Override
    public void addFavorite(UUID noteId) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        noteDao.addFavorite(currentUserId, noteId);
    }

    @Transactional
    @Override
    public void removeFavorite(UUID noteId) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = noteDao.removeFavorite(currentUserId, noteId);
        if (!success) throw new InvalidNoteException();
    }
}
