package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.exceptions.UnavailableNameException;
import ar.edu.itba.paw.models.exceptions.UserNotOwnerException;
import ar.edu.itba.paw.models.exceptions.note.InvalidNoteException;
import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.NoteDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceImplTest {
    @Mock
    private NoteDao noteDao;

    @Mock
    private DirectoryDao directoryDao;

    @Mock
    private SecurityService securityService;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    public void testCreateNote() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Directory dirToReturn = Mockito.mock(Directory.class);
        Subject subject = mockSubject();
        given(dirToReturn.getSubject()).willAnswer(invocation -> subject);
        Mockito.when(directoryDao.getDirectoryRoot(Mockito.any())).thenReturn(Optional.of(dirToReturn));
        UUID expectedNoteId = UUID.randomUUID();
        Mockito.when(noteDao.create(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(expectedNoteId);
        Mockito.when(searchService.findByName(Mockito.any(), Mockito.anyString())).thenReturn(Optional.empty());
        UUID noteId = noteService.createNote("new", UUID.randomUUID(), true, new byte[0], "" , Category.EXAM.getFormattedName());
        assertEquals(expectedNoteId, noteId);
    }

    @Test(expected = UnavailableNameException.class)
    public void testCreateNoteNameUsed() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        UUID expectedNoteId = UUID.randomUUID();
        Mockito.when(searchService.findByName(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(UUID.randomUUID()));
        noteService.createNote("new", UUID.randomUUID(), true, new byte[0], "" , Category.EXAM.getFormattedName());
        fail();
    }

    @Test(expected = NoteNotFoundException.class)
    public void testUpdateNoteNotFound() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        noteService.update(UUID.randomUUID(), "new", true, Category.EXAM.getFormattedName());
        fail();
    }

    @Test(expected = UserNotOwnerException.class)
    public void testUpdateNoteUserNotOwner() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(
                new User.UserBuilder().userId(SAIDMAN_ID).build()
        );
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Note.NoteBuilder().user(mockUser()).build()));
        noteService.update(UUID.randomUUID(), "new", true, Category.EXAM.getFormattedName());
        fail();
    }

    @Test(expected = UnavailableNameException.class)
    public void testUpdateNoteUnavailableName() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Note.NoteBuilder().user(mockUser()).build()));
        Mockito.when(searchService.findByName(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(UUID.randomUUID()));
        noteService.update(UUID.randomUUID(), "new", true, Category.EXAM.getFormattedName());
        fail();
    }

    @Test(expected = NoteNotFoundException.class)
    public void testDeleteNoteFailureNotFound() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockAdmin());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        noteService.delete(UUID.randomUUID(), "reason");
        fail();
    }

    @Test(expected = InvalidNoteException.class)
    public void testDeleteNoteFailureAdmin() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockAdmin());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(new Note.NoteBuilder().build()));
        noteService.delete(UUID.randomUUID(), "reason");
        fail();
    }

    @Test(expected = InvalidNoteException.class)
    public void testDeleteNoteFailure() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(new Note.NoteBuilder().build()));
        Mockito.when(noteDao.delete(Mockito.any(), Mockito.any())).thenReturn(false);
        noteService.delete(UUID.randomUUID(), "reason");
        fail();
    }

    @Test
    public void testDeleteReviewFailure() {
        Mockito.when(noteDao.getReview(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Review(new Note.NoteBuilder().build(), mockUser(), 5, "wowie")));
        Mockito.when(noteDao.deleteReview(Mockito.any(), Mockito.any())).thenReturn(false);
        boolean deleted = noteService.deleteReview(UUID.randomUUID(), UUID.randomUUID(), "Inappropriate");
        assertFalse(deleted);
    }

    @Test(expected = InvalidReviewException.class)
    public void testAddReviewSameUser() {
        User mUser = mockUser();
        Note mNote = new Note.NoteBuilder().user(mUser).build();
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mUser);
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(mNote));
        noteService.createReview(UUID.randomUUID(), 5, "my note is great");
        fail();
    }

    @Test
    public void testGetReviewsSuccess() {
        final int PAGE_SIZE = 10;
        final int PAGE = 2;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(noteDao.countReviews(Mockito.any(), Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(noteDao.getReviews(Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(new Note.NoteBuilder().build()));

        Page<Review> results = noteService.getReviews(UUID.randomUUID(), null, PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(PAGE, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetReviewsOverPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = 6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(noteDao.countReviews(Mockito.any(), Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(noteDao.getReviews(Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(new Note.NoteBuilder().build()));

        Page<Review> results = noteService.getReviews(UUID.randomUUID(), null, PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(results.getTotalPages(), results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetReviewsUnderPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = -6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(noteDao.countReviews(Mockito.any(), Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(noteDao.getReviews(Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(new Note.NoteBuilder().build()));

        Page<Review> results = noteService.getReviews(UUID.randomUUID(), null, PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(1, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }


    @Test
    public void testGetReviewsByUserSuccess() {
        final int PAGE_SIZE = 10;
        final int PAGE = 2;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(noteDao.countReviewsByTargetUser(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(noteDao.getReviewsByTargetUser(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<Review> results = noteService.getReviewsByTargetUser(UUID.randomUUID(), PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(PAGE, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetReviewsByUserOverPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = 6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(noteDao.countReviewsByTargetUser(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(noteDao.getReviewsByTargetUser(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<Review> results = noteService.getReviewsByTargetUser(UUID.randomUUID(), PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(results.getTotalPages(), results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetReviewsByUserUnderPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = -6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(noteDao.countReviewsByTargetUser(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(noteDao.getReviewsByTargetUser(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<Review> results = noteService.getReviewsByTargetUser(UUID.randomUUID(), PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(1, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test(expected = NoteNotFoundException.class)
    public void testRemoveFavoriteInvalid() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        noteService.removeFavorite(UUID.randomUUID());
        fail();
    }
}
