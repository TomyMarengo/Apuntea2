package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectCareerException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectException;
import ar.edu.itba.paw.persistence.CareerDao;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.SearchDao;
import ar.edu.itba.paw.persistence.SubjectDao;

import static ar.edu.itba.paw.services.ServiceTestUtils.mockDirectory;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceImplTest {
    @Mock
    private SubjectDao subjectDao;
    @Mock
    private CareerDao careerDao;
    @Mock
    private DirectoryDao directoryDao;
    @Mock
    private SearchDao searchDao;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test(expected = InvalidSubjectException.class)
    public void testLinkSubjectToCareerInvalidSubjectCareer() {
        UUID subjectId = UUID.randomUUID();
        UUID careerId = UUID.randomUUID();
        int year = 1;

        subjectService.linkSubjectToCareer(subjectId, careerId, year);

        fail();
    }

    @Test(expected = InvalidSubjectException.class)
    public void testUpdateSubjectInvalidSubject() {
        UUID subjectId = UUID.randomUUID();
        String name = "Subject 1a";

        subjectService.updateSubject(subjectId, name);

        fail();
    }

    @Test(expected = InvalidSubjectException.class)
    public void testUpdateSubjectCareerFailureUpdateSubject() {
        UUID subjectId = UUID.randomUUID();
        UUID careerId = UUID.randomUUID();
        String newName = "Subject 1a";
        int year = 1;

        subjectService.updateSubjectCareer(subjectId, newName, careerId, year);

        fail();
    }

    @Test(expected = InvalidSubjectException.class)
    public void testUpdateSubjectCareerInvalidSubjectCareer() {
        UUID subjectId = UUID.randomUUID();
        UUID careerId = UUID.randomUUID();
        String newName = "Subject 1a";
        int year = 1;

        subjectService.updateSubjectCareer(subjectId, newName, careerId, year);

        fail();
    }

    @Test(expected = InvalidSubjectException.class)
    public void testUnlinkSubjectFromCareerFailureGetSubjectById() {
        UUID subjectId = UUID.randomUUID();
        UUID careerId = UUID.randomUUID();
        Mockito.when(subjectDao.getSubjectById(subjectId)).thenReturn(
                Optional.empty()
        );

        subjectService.unlinkSubjectFromCareer(subjectId, careerId);
        fail();
    }

    @Test(expected = InvalidSubjectCareerException.class)
    public void testUnlinkSubjectFromCareerFailureUnlinkSubject() {
        UUID subjectId = UUID.randomUUID();
        UUID directoryId = UUID.randomUUID();
        UUID careerId = UUID.randomUUID();
        Mockito.when(subjectDao.getSubjectById(subjectId)).thenReturn(
                Optional.of(new Subject("Subject 1a", mockDirectory("1a")))
        );
        Mockito.when(subjectDao.unlinkSubjectFromCareer(Mockito.any(), Mockito.any())).thenReturn(false);

        subjectService.unlinkSubjectFromCareer(subjectId, careerId);
        fail();
    }

    @Test(expected = InvalidSubjectException.class)
    public void testUnlinkSubjectFromCareerDeletionFailureDeleteSubject() {
        UUID subjectId = UUID.randomUUID();
        UUID directoryId = UUID.randomUUID();
        UUID careerId = UUID.randomUUID();
        Mockito.when(subjectDao.getSubjectById(subjectId)).thenReturn(
                Optional.of(new Subject("Subject 1a", mockDirectory("1a")))
        );
        Mockito.when(subjectDao.unlinkSubjectFromCareer(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(careerDao.countCareersBySubjectId(Mockito.any())).thenReturn(0);
        Mockito.when(searchDao.countChildren(Mockito.any())).thenReturn(0);
        Mockito.when(subjectDao.delete(Mockito.any())).thenReturn(false);

        subjectService.unlinkSubjectFromCareer(subjectId, careerId);
        fail();
    }

    @Test(expected = InvalidDirectoryException.class)
    public void testUnlinkSubjectFromCareerDeletionFailureDeleteRootDirectory() {
        UUID subjectId = UUID.randomUUID();
        UUID directoryId = UUID.randomUUID();
        UUID careerId = UUID.randomUUID();
        Mockito.when(subjectDao.getSubjectById(subjectId)).thenReturn(
                Optional.of(new Subject("Subject 1a", mockDirectory("1a")))
        );
        Mockito.when(subjectDao.unlinkSubjectFromCareer(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(careerDao.countCareersBySubjectId(Mockito.any())).thenReturn(0);
        Mockito.when(searchDao.countChildren(Mockito.any())).thenReturn(0);
        Mockito.when(subjectDao.delete(Mockito.any())).thenReturn(true);
        Mockito.when(directoryDao.delete(Mockito.any())).thenReturn(false);

        subjectService.unlinkSubjectFromCareer(subjectId, careerId);
        fail();
    }

}
