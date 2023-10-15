package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
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

    @Test(expected = InvalidDirectoryException.class)
    public void testDeleteAdminInvalidIds() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockAdmin());
        Mockito.when(directoryDao.delete(Mockito.any())).thenReturn(Collections.emptyList()); // The deletion failed
        directoryService.delete(new UUID[]{EDA_DIRECTORY_ID, MVC_DIRECTORY_ID}, "lol");
        Assert.fail();
    }

    @Test
    public void testDeleteAdminValidIds() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockAdmin());
        Mockito.when(directoryDao.delete(Mockito.any())).thenReturn(Arrays.asList(mockDirectory("theory"), mockDirectory("MVC")));
        directoryService.delete(new UUID[]{THEORY_DIRECTORY_ID, MVC_DIRECTORY_ID}, "lmao");
    }

    @Test(expected = InvalidDirectoryException.class)
    public void testDeleteNotAdminInvalidIds() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(directoryDao.delete(Mockito.any(), Mockito.any())).thenReturn(false); // The deletion failed
        directoryService.delete(new UUID[]{EDA_DIRECTORY_ID, MVC_DIRECTORY_ID}, null);
        Assert.fail();
    }

    @Test
    public void testDeleteNotAdminValidIds() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(directoryDao.delete(Mockito.any(), Mockito.any())).thenReturn(true);
        directoryService.delete(new UUID[]{EDA_DIRECTORY_ID, MVC_DIRECTORY_ID}, null);
    }
}
