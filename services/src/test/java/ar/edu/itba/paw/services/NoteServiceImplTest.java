package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.note.InvalidNoteException;
import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

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
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
//        Mockito.when(directoryDao.getSubjectByDirectory(Mockito.any())).thenReturn(mockSubject());
        CommonsMultipartFile noteFile = Mockito.mock(CommonsMultipartFile.class);
        given(noteFile.getBytes()).willAnswer(invocation -> {throw new IOException();});
        noteService.createNote("new", UUID.randomUUID(), true, noteFile , Category.EXAM.getFormattedName());
        fail();
    }

    @Test
    public void testCreateNote() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(directoryDao.getDirectoryPath(Mockito.any())).thenReturn(new DirectoryPath(Collections.singletonList(mockRootDirectory("levenshtein"))));
        CommonsMultipartFile noteFile = Mockito.mock(CommonsMultipartFile.class);
        Mockito.when(noteFile.getOriginalFilename()).thenReturn("test.pdf");
        Mockito.when(noteFile.getBytes()).thenReturn(new byte[0]);
        UUID expectedNoteId = UUID.randomUUID();
        Mockito.when(noteDao.create(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(expectedNoteId);
        UUID noteId = noteService.createNote("new", UUID.randomUUID(), true, noteFile , Category.EXAM.getFormattedName());
        assertEquals(expectedNoteId, noteId);
    }

    @Test(expected = InvalidNoteException.class)
    public void testUpdateNoteFailure() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
//        Mockito.when(noteDao.update(Mockito.any(), Mockito.any())).thenReturn(false);
        noteService.update(UUID.randomUUID(), "new", true, Category.EXAM.getFormattedName());
        fail();
    }

    @Test(expected = InvalidNoteException.class)
    public void testDeleteNoteFailureAdmin() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockAdmin());
        Mockito.when(noteDao.delete(Mockito.any())).thenReturn(false);
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
        Mockito.when(noteDao.getReview(Mockito.any(), Mockito.any())).thenReturn(new Review(new Note.NoteBuilder().build(), mockUser(), "wowie", 5));
        Mockito.when(noteDao.deleteReview(Mockito.any(), Mockito.any())).thenReturn(false);
        noteService.deleteReview(UUID.randomUUID(), UUID.randomUUID(), "Inappropriate");
        fail();
    }

    @Test(expected = InvalidReviewException.class)
    public void testAddReviewSameUser() {
        User mUser = mockUser();
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mUser);
        Mockito.when(noteDao.getNoteById(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(new Note.NoteBuilder().user(mUser).build()));
        Mockito.when(noteDao.createOrUpdateReview(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Review(new Note.NoteBuilder().user(mUser).build(), mUser, "my note is great", 5));
        noteService.createOrUpdateReview(UUID.randomUUID(), 5, "my note is great");
        fail();
    }
}
