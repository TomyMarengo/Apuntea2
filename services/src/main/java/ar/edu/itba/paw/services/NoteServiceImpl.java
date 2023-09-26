package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
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
    public UUID createNote(MultipartFile file, String name, UUID subjectId, String category) throws IOException {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        return noteDao.create(file.getBytes(), name, userId, subjectId, category, FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    @Transactional
    @Override
    public UUID createNote(MultipartFile file, String name, String category, UUID parentId) throws IOException {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        UUID subjectId = directoryDao.getDirectoryPath(parentId)
                                        .getRootDirectory()
                                        .getSubject()
                                        .getSubjectId();
        return noteDao.create(file.getBytes(), name, userId, subjectId, category, parentId, FilenameUtils.getExtension(file.getOriginalFilename()));
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
    public List<Review> getReviews(UUID noteId) {
        return noteDao.getReviews(noteId);
    }

}
