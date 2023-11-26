package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.note.InvalidNoteException;
import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.NoteDao;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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

    @InjectMocks
    private NoteServiceImpl noteService;


    @Test(expected = InvalidFileException.class)
    public void testCreateNoteInvalidFile() {
        Directory dirToReturn = Mockito.mock(Directory.class);
        Mockito.when(directoryDao.getDirectoryRoot(Mockito.any())).thenReturn(Optional.of(dirToReturn));
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        CommonsMultipartFile noteFile = Mockito.mock(CommonsMultipartFile.class);
        given(noteFile.getBytes()).willAnswer(invocation -> {throw new IOException();});
        noteService.createNote("new", UUID.randomUUID(), true, noteFile , Category.EXAM.getFormattedName());
        fail();
    }

    @Test
    public void testCreateNote() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        CommonsMultipartFile noteFile = Mockito.mock(CommonsMultipartFile.class);
        Directory dirToReturn = Mockito.mock(Directory.class);
        Subject subject = mockSubject();
        given(dirToReturn.getSubject()).willAnswer(invocation -> subject);
        Mockito.when(directoryDao.getDirectoryRoot(Mockito.any())).thenReturn(Optional.of(dirToReturn));
        Mockito.when(noteFile.getOriginalFilename()).thenReturn("test.pdf");
        Mockito.when(noteFile.getBytes()).thenReturn(new byte[]{1});
        UUID expectedNoteId = UUID.randomUUID();
        Mockito.when(noteDao.create(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(expectedNoteId);
        UUID noteId = noteService.createNote("new", UUID.randomUUID(), true, noteFile , Category.EXAM.getFormattedName());
        assertEquals(expectedNoteId, noteId);
    }

    @Test(expected = InvalidNoteException.class)
    public void testUpdateNoteFailure() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        noteService.update(UUID.randomUUID(), "new", true, Category.EXAM.getFormattedName());
        fail();
    }

    @Test(expected = InvalidNoteException.class)
    public void testDeleteNoteFailureAdmin() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockAdmin());
        noteService.delete(new UUID[]{UUID.randomUUID()}, "reason");
        fail();
    }

    @Test(expected = InvalidNoteException.class)
    public void testDeleteNoteFailure() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(noteDao.delete(Mockito.any(), Mockito.any())).thenReturn(false);
        noteService.delete(new UUID[]{UUID.randomUUID()}, "reason");
        fail();
    }

    @Test(expected = InvalidReviewException.class)
    public void testDeleteReviewFailure() {
        Mockito.when(noteDao.getReview(Mockito.any(), Mockito.any())).thenReturn(new Review(new Note.NoteBuilder().build(), mockUser(), 5, "wowie"));
        Mockito.when(noteDao.deleteReview(Mockito.any(), Mockito.any())).thenReturn(false);
        noteService.deleteReview(UUID.randomUUID(), UUID.randomUUID(), "Inappropriate");
        fail();
    }

    @Test(expected = InvalidReviewException.class)
    public void testAddReviewSameUser() {
        User mUser = mockUser();
        Note mNote = new Note.NoteBuilder().user(mUser).build();
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mUser);
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(mNote));
        noteService.createOrUpdateReview(UUID.randomUUID(), 5, "my note is great");
        fail();
    }
}
