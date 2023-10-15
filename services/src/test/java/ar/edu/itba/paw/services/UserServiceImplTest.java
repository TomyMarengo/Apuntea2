package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.user.InvalidUserException;
import ar.edu.itba.paw.models.user.ProfilePicture;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.UserDao;
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

import static ar.edu.itba.paw.services.ServiceTestUtils.mockUser;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)

public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetStudentsSuccess() {
        final int PAGE_SIZE = 10;
        final int PAGE = 2;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(userDao.getStudentsQuantity(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getStudents(null, PAGE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(PAGE, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetUsersOverPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = 6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(userDao.getStudentsQuantity(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getStudents(null, PAGE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(results.getTotalPages(), results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetUsersUnderPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = -6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(userDao.getStudentsQuantity(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getStudents(null, PAGE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(1, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test(expected = InvalidUserException.class)
    public void testCouldNotUpdateProfile() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(userDao.update(Mockito.any())).thenReturn(false);
        userService.updateProfile("firstName", "lastName", "username", null);
        fail();
    }

    @Test(expected = InvalidFileException.class)
    public void testInvalidFileUpdatingProfile() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        Mockito.when(userDao.update(Mockito.any())).thenReturn(true);
        CommonsMultipartFile profilePicture = Mockito.mock(CommonsMultipartFile.class);
        Mockito.when(profilePicture.isEmpty()).thenReturn(false);
        given(profilePicture.getBytes()).willAnswer(invocation -> {throw new IOException();});
        userService.updateProfile("firstName", "lastName", "username", profilePicture);
        fail();
    }

    @Test
    public void testGetProfilePictureMap() {
        Mockito.when(userDao.getProfilePicture(Mockito.any())).thenReturn(Optional.of(new ProfilePicture(UUID.randomUUID().toString(), new byte[]{1, 2, 3})));
        Optional<byte[]> maybePic = userService.getProfilePicture(UUID.randomUUID());
        assertTrue(maybePic.isPresent());
        assertArrayEquals(new byte[]{1, 2, 3}, maybePic.get());
    }

    @Test
    public void testGetProfilePictureEmpty() {
        Mockito.when(userDao.getProfilePicture(Mockito.any())).thenReturn(Optional.empty());
        Optional<byte[]> maybePic = userService.getProfilePicture(UUID.randomUUID());
        assertFalse(maybePic.isPresent());
    }

    @Test(expected = InvalidUserException.class)
    public void testBanUserError() {
        Mockito.when(securityService.getAdminOrThrow()).thenReturn(mockUser());
        Mockito.when(userDao.banUser(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        userService.banUser(UUID.randomUUID(), "Hm?");
        fail();
    }

}
