package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.institutional.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectCareerException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectException;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.*;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceImplTest {
    @Mock
    private UserDao userDao;
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


    @Test(expected = UserNotFoundException.class)
    public void testGetSubjectsByUserIdGroupNonExistentUser() {
        Mockito.when(userDao.findById(Mockito.any())).thenReturn(Optional.empty());
        subjectService.getSubjects(UUID.randomUUID(), null, UUID.randomUUID());
        fail();
    }

}
