package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.exceptions.UnavailableNameException;
import ar.edu.itba.paw.models.exceptions.UserNotOwnerException;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.exceptions.note.InvalidNoteException;
import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.models.exceptions.note.ReviewNotFoundException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.NoteDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class  NoteServiceImpl implements NoteService {
    private final UserDao userDao;
    private final NoteDao noteDao;
    private final DirectoryDao directoryDao;
    private final EmailService emailService;
    private final SecurityService securityService;

    private final SearchService searchService;

    @Autowired
    public NoteServiceImpl(final UserDao userDao, final NoteDao noteDao, final DirectoryDao directoryDao, final SecurityService securityService, final EmailService emailService, final SearchService searchService) {
        this.userDao = userDao;
        this.noteDao = noteDao;
        this.securityService = securityService;
        this.emailService = emailService;
        this.directoryDao = directoryDao;
        this.searchService = searchService;
    }

    @Transactional
    @Override
    public UUID createNote(final String name, final UUID parentId, final boolean visible, final byte[] file, final String mimeType, final String category) {
        User user = securityService.getCurrentUserOrThrow();
        if (searchService.findByName(parentId, name).isPresent()) throw new UnavailableNameException();

        Directory rootDir = directoryDao.getDirectoryRoot(parentId).orElseThrow(InvalidDirectoryException::new);
        Subject subject = rootDir.getSubject();
        return noteDao.create(name, subject.getSubjectId(), user, parentId, visible, file, category, mimeType);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        final Optional<User> maybeUser = securityService.getCurrentUser();
        final Optional<Note> note = noteDao.getNoteById(noteId, maybeUser.map(User::getUserId).orElse(null));
        if (note.isPresent() && maybeUser.isPresent()) {
//            noteDao.loadNoteFavorites(Collections.singletonList(note.get().getId()), maybeUser.get().getUserId());
            noteDao.addInteractionIfNotExists(maybeUser.get(), note.get());
        }
        return note;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<NoteFile> getNoteFileById(UUID noteId) {
        final UUID userId = securityService.getCurrentUser().map(User::getUserId).orElse(null);
        return noteDao.getNoteFileById(noteId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Note> getNotes(final UUID parentId, final UUID userId, final UUID favBy, final String category, final String word,
                               final UUID institutionId, final UUID careerId, final UUID subjectId,
                               final String sortBy, final boolean ascending, final int page, final int pageSize) {
        final SearchArguments.SearchArgumentsBuilder sab = new SearchArguments.SearchArgumentsBuilder()
                .userId(userId)
                .parentId(parentId)
                .favBy(favBy)
                .category(category)
                .word(word)
                .institutionId(institutionId).careerId(careerId).subjectId(subjectId)
                .sortBy(sortBy).ascending(ascending);
        final Optional<User> maybeUser = securityService.getCurrentUser();
        maybeUser.ifPresent(u -> sab.currentUserId(u.getUserId()));

        boolean navigate = parentId != null || favBy != null; // Maybe this should be an additional parameter

        SearchArguments searchArgumentsWithoutPaging = sab.build();
        int countTotalResults = navigate? noteDao.countNavigationResults(searchArgumentsWithoutPaging) : noteDao.countSearchResults(searchArgumentsWithoutPaging);
        int safePage = Page.getSafePagePosition(page, countTotalResults, pageSize);

        sab.page(safePage).pageSize(pageSize);
        SearchArguments sa = sab.build();

        return new Page<>(
                navigate? noteDao.navigate(sa) : noteDao.search(sa),
                sa.getPage(),
                sa.getPageSize(),
                countTotalResults
        );
    }



    @Transactional
    @Override
    public void update(UUID noteId, String name, Boolean visible, String category) {
        User currentUser = securityService.getCurrentUserOrThrow();
        Note note = noteDao.getNoteById(noteId, currentUser.getUserId()).orElseThrow(NoteNotFoundException::new);
        if (!currentUser.equals(note.getUser()))
            throw new UserNotOwnerException();
        if (name != null) {
            if (searchService.findByName(note.getParentId(), name).isPresent()) throw new UnavailableNameException();
            note.setName(name);
        }
        if (visible != null)
            note.setVisible(visible);
        if (category != null)
            note.setCategory(Category.valueOf(category.toUpperCase()));
    }

    @Transactional
    @Override
    public void delete(UUID noteId, String reason) {
        User currentUser = securityService.getCurrentUserOrThrow();
        Note note = noteDao.getNoteById(noteId, currentUser.getUserId()).orElseThrow(NoteNotFoundException::new);
        if (!currentUser.isAdmin()) {
            if (!noteDao.delete(noteId, currentUser.getUserId()))
                throw new UserNotOwnerException();
        } else {
            if (!noteDao.delete(noteId)) throw new InvalidNoteException();
            emailService.sendDeleteNoteEmail(note, reason);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> getReview(UUID noteId, UUID userId) {
        return noteDao.getReview(noteId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Review> getReviews(UUID noteId, UUID userId, int pageNum, int pageSize) {
        if (noteId != null)
            noteDao.getNoteById(noteId, securityService.getCurrentUser().map(User::getUserId).orElse(null)).orElseThrow(NoteNotFoundException::new);
        if (userId != null)
            userDao.findById(userId).orElseThrow(UserNotFoundException::new);

        int countTotalResults = noteDao.countReviews(noteId, userId);
        int safePage = Page.getSafePagePosition(pageNum, countTotalResults, pageSize);

        return new Page<>(
                noteDao.getReviews(noteId, userId, safePage, pageSize),
                safePage,
                pageSize,
                countTotalResults
        );
    }

    @Transactional
    @Override
    public Page<Review> getReviewsByTargetUser(UUID targetUserId, int pageNum, int pageSize) {
        int countTotalResults = noteDao.countReviewsByTargetUser(targetUserId);
        int safePage = Page.getSafePagePosition(pageNum, countTotalResults, pageSize);

        return new Page<>(
                noteDao.getReviewsByTargetUser(targetUserId, safePage, pageSize),
                safePage,
                pageSize,
                countTotalResults
        );
    }

    @Transactional
    @Override
    public Review createReview(UUID noteId, int score, String content) {
        User user = securityService.getCurrentUserOrThrow();
        Note note = noteDao.getNoteById(noteId, user.getUserId()).orElseThrow(NoteNotFoundException::new);
        if (note.getUser().equals(user))
            throw new InvalidReviewException();
        if (noteDao.getReview(noteId, user.getUserId()).isPresent())
            return null;
        Review review = noteDao.createOrUpdateReview(note, user, score, content);
        emailService.sendReviewEmail(review);
        return review;
    }

    @Transactional
    @Override
    public Review updateReview(UUID noteId, int score, String content) {
        User user = securityService.getCurrentUserOrThrow();
        Note note = noteDao.getNoteById(noteId, user.getUserId()).orElseThrow(NoteNotFoundException::new);
        noteDao.getReview(noteId, user.getUserId()).orElseThrow(ReviewNotFoundException::new);
        if (note.getUser().equals(user))
            throw new InvalidReviewException();
        return noteDao.createOrUpdateReview(note, user, score, content);
    }

    @Transactional
    @Override
    public boolean deleteReview(UUID noteId, UUID userId, String reason) {
        Optional<Review> maybeReview = noteDao.getReview(noteId, userId);
        if (!maybeReview.isPresent()) return false;
        if (noteDao.deleteReview(noteId, userId)) {
            emailService.sendDeleteReviewEmail(maybeReview.get(), reason);
            return true;
        }
        return false;
    }

//    @Transactional
//    @Override
//    public Collection<Note> getFavorites() {
//        User currentUser = securityService.getCurrentUserOrThrow();
//        return currentUser.getNoteFavorites();
//    }

    @Transactional
    @Override
    public boolean addFavorite(UUID noteId) {
        User user = securityService.getCurrentUserOrThrow();
        noteDao.getNoteById(noteId, user.getUserId()).orElseThrow(NoteNotFoundException::new);
        return noteDao.addFavorite(user.getUserId(), noteId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isFavorite(UUID noteId) {
        User user = securityService.getCurrentUserOrThrow();
        noteDao.getNoteById(noteId, user.getUserId()).orElseThrow(NoteNotFoundException::new);
        return noteDao.isFavorite(user.getUserId(), noteId);
    }

    @Transactional
    @Override
    public boolean removeFavorite(UUID noteId) {
        User user = securityService.getCurrentUserOrThrow();
        noteDao.getNoteById(noteId, user.getUserId()).orElseThrow(NoteNotFoundException::new);
        return noteDao.removeFavorite(user.getUserId(), noteId);
    }
}
