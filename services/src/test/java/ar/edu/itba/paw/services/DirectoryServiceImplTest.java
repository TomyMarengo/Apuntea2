package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryFavoriteGroups;
import ar.edu.itba.paw.models.exceptions.UserNotOwnerException;
import ar.edu.itba.paw.models.exceptions.directory.DirectoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.DirectoryDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryServiceImplTest {
    @Mock
    private SecurityService securityService;

    @Mock
    private DirectoryDao directoryDao;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private DirectoryServiceImpl directoryService;

    @Test(expected = DirectoryNotFoundException.class)
    public void testUpdateDirectoryInvalid() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Directory mockdir = mockDirectory("dir");
        directoryService.update(mockdir.getId(), mockdir.getName(), true, "BBBBBB");
        Assert.fail();
    }

    @Test(expected = DirectoryNotFoundException.class)
    public void testDeleteAdminInvalidIds() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockAdmin());
        directoryService.delete(EDA_DIRECTORY_ID, "lol");
        Assert.fail();
    }

    @Test(expected = UserNotOwnerException.class)
    public void testDeleteNotAdminNotOwnerId() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(directoryDao.delete(Mockito.any(), Mockito.any())).thenReturn(false); // The deletion failed
        directoryService.delete(EDA_DIRECTORY_ID, null);
        Assert.fail();
    }
}
